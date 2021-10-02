package com.gempukku.libgdx.graph.plugin.models.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderLoaderCallback;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelImpl;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ModelShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ModelShaderConfiguration()};
    private PluginPrivateDataSource pluginPrivateDataSource;

    public ModelShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ModelShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ModelShaderContextImpl shaderContext = new ModelShaderContextImpl(pluginPrivateDataSource);

        final Array<ShaderGroup> opaqueShaderGroups = new Array<>();
        final Array<ShaderGroup> translucentShaderGroups = new Array<>();
        final ObjectSet<String> opaqueShaderTags = new ObjectSet<>();
        final ObjectSet<String> translucentShaderTags = new ObjectSet<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            ShaderGroup shaderGroup = new ShaderGroup(shaderDefinition, whitePixel);
            shaderGroup.initialize();

            if (shaderGroup.getColorShader().getBlending() == BasicShader.Blending.opaque) {
                opaqueShaderGroups.add(shaderGroup);
                opaqueShaderTags.add(shaderGroup.getTag());
            } else {
                translucentShaderGroups.add(shaderGroup);
                translucentShaderTags.add(shaderGroup.getTag());
            }
        }

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                GraphModelsImpl graphModels = pipelineInitializationFeedback.getPrivatePluginData(GraphModelsImpl.class);
                for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                    ModelGraphShader shader = shaderGroup.getColorShader();
                    graphModels.registerTag(shader.getTag(), shader);
                }
                for (ShaderGroup shaderGroup : translucentShaderGroups) {
                    ModelGraphShader shader = shaderGroup.getColorShader();
                    graphModels.registerTag(shader.getTag(), shader);
                }
            }

            private void initializeDepthShaders() {
                for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                    shaderGroup.initializeDepthShader();
                }
            }

            public boolean needsDepth(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                for (ShaderGroup shaderGroup : translucentShaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingColorTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                for (ShaderGroup shaderGroup : translucentShaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingColorTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);

                GraphModelsImpl models = pipelineRenderingContext.getPrivatePluginData(GraphModelsImpl.class);

                boolean needsToDrawDepth = pipelineRequirements.isRequiringDepthTexture();
                boolean usesDepth = false;
                if (enabled) {
                    if (needsToDrawDepth)
                        initializeDepthShaders();
                    if (needsDepth(models)) {
                        usesDepth = true;
                        pipelineRequirements.setRequiringDepthTexture();
                    }
                }

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                if (enabled) {
                    boolean needsSceneColor = isRequiringSceneColor(models);

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                    Camera camera = cameraInput.getValue(pipelineRenderingContext, null);

                    shaderContext.setCamera(camera);
                    shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    if (needsToDrawDepth || usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                        shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                    }

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    currentBuffer.beginColor();

                    // First render opaque models
                    models.prepareForRendering(camera, opaqueShaderTags);
                    models.orderFrontToBack();
                    for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                        ModelGraphShader colorShader = shaderGroup.getColorShader();
                        String tag = colorShader.getTag();
                        renderWithShaderOpaquePass(tag, colorShader, models, shaderContext, pipelineRenderingContext.getRenderContext());
                    }

                    // Then if depth buffer is needed for later passes - draw to it
                    if (needsToDrawDepth) {
                        currentBuffer.endColor();
                        currentBuffer.beginDepth();

                        for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                            ModelGraphShader depthShader = shaderGroup.getDepthShader();
                            if (depthShader != null) {
                                String tag = depthShader.getTag();
                                renderWithShaderOpaquePass(tag, depthShader, models, shaderContext, pipelineRenderingContext.getRenderContext());
                            }
                        }

                        currentBuffer.endDepth();
                        currentBuffer.beginColor();
                    }

                    // Then render transparent models
                    models.prepareForRendering(camera, translucentShaderTags);
                    models.orderBackToFront();
                    GraphShader lastShader = null;
                    for (GraphModelImpl graphModel : models.getModels()) {
                        for (ShaderGroup shaderGroup : translucentShaderGroups) {
                            ModelGraphShader colorShader = shaderGroup.getColorShader();
                            String tag = colorShader.getTag();
                            if (graphModel.getTag().equals(tag) &&
                                    graphModel.getRenderableModel().isRendered(shaderContext.getCamera())) {
                                if (lastShader != colorShader) {
                                    if (lastShader != null)
                                        lastShader.end();
                                    shaderContext.setGlobalPropertyContainer(models.getGlobalProperties(tag));
                                    colorShader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                                    lastShader = colorShader;
                                }
                                colorShader.render(shaderContext, graphModel);
                            }
                        }
                    }
                    if (lastShader != null)
                        lastShader.end();

                    if (sceneColorBuffer != null)
                        renderPipeline.returnFrameBuffer(sceneColorBuffer);

                    currentBuffer.endColor();
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private RenderPipelineBuffer setupColorTexture(final RenderPipeline renderPipeline, final RenderPipelineBuffer currentBuffer,
                                                           PipelineRenderingContext pipelineRenderingContext) {
                RenderPipelineBuffer sceneColorBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);
                shaderContext.setColorTexture(sceneColorBuffer.getColorBufferTexture());
                renderPipeline.drawTexture(currentBuffer, sceneColorBuffer, pipelineRenderingContext);
                return sceneColorBuffer;
            }

            private void renderWithShaderOpaquePass(String tag, ModelGraphShader shader, GraphModelsImpl models, ModelShaderContextImpl shaderContext,
                                                    RenderContext renderContext) {
                boolean begun = false;
                for (GraphModelImpl graphModel : models.getModels()) {
                    if (graphModel.getTag().equals(tag) &&
                            graphModel.getRenderableModel().isRendered(shaderContext.getCamera())) {
                        if (!begun) {
                            shaderContext.setGlobalPropertyContainer(models.getGlobalProperties(tag));
                            shader.begin(shaderContext, renderContext);
                            begun = true;
                        }
                        shader.render(shaderContext, graphModel);
                    }
                }
                if (begun)
                    shader.end();
            }

            @Override
            public void dispose() {
                for (ShaderGroup shaderGroup : opaqueShaderGroups) {
                    shaderGroup.dispose();
                }
                for (ShaderGroup shaderGroup : translucentShaderGroups) {
                    shaderGroup.dispose();
                }
                whitePixel.dispose();
            }
        };
    }

    private static ModelGraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        ModelGraphShader modelGraphShader = GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(defaultTexture, false, configurations), PropertyLocation.Uniform);
        modelGraphShader.setTag(tag);
        return modelGraphShader;
    }

    private static ModelGraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        ModelGraphShader modelGraphShader = GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(defaultTexture, true, configurations), PropertyLocation.Uniform);
        modelGraphShader.setTag(tag);
        return modelGraphShader;
    }

    private class ShaderGroup implements Disposable {
        private JsonValue shaderDefinition;
        private WhitePixel whitePixel;
        private ModelGraphShader colorShader;
        private ModelGraphShader depthShader;
        private IntArray colorShaderAttributeLocations = new IntArray();
        private IntArray depthShaderAttributeLocations = new IntArray();

        public ShaderGroup(JsonValue shaderDefinition, WhitePixel whitePixel) {
            this.shaderDefinition = shaderDefinition;
            this.whitePixel = whitePixel;
        }

        public void initialize() {
            colorShader = ModelShaderRendererPipelineNodeProducer.createColorShader(shaderDefinition, whitePixel.texture);
        }

        public void initializeDepthShader() {
            if (depthShader == null && colorShader.getBlending() == BasicShader.Blending.opaque) {
                depthShader = ModelShaderRendererPipelineNodeProducer.createDepthShader(shaderDefinition, whitePixel.texture);
            }
        }

        public ModelGraphShader getColorShader() {
            return colorShader;
        }

        public ModelGraphShader getDepthShader() {
            return depthShader;
        }

        public String getTag() {
            return colorShader.getTag();
        }

        @Override
        public void dispose() {
            colorShader.dispose();
            if (depthShader != null)
                depthShader.dispose();
        }
    }
}
