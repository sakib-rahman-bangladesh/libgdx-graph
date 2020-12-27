package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsUniformShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.model.ModelGraphShader;
import com.gempukku.libgdx.graph.shader.model.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.model.ModelShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.model.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.model.impl.IGraphModelInstance;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ModelShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ModelShaderConfiguration()};

    public ModelShaderRendererPipelineNodeProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ModelShaderContextImpl shaderContext = new ModelShaderContextImpl();

        final Array<ShaderGroup> shaderGroups = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            ShaderGroup shaderGroup = new ShaderGroup(shaderDefinition, whitePixel);
            shaderGroup.initialize();
            shaderGroups.add(shaderGroup);
        }

        final PipelineNode.FieldOutput<GraphShaderEnvironment> lightsInput = (PipelineNode.FieldOutput<GraphShaderEnvironment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader shader = shaderGroup.getColorShader();
                    pipelineInitializationFeedback.registerModelAttribute(shader.getTag(), shader.getVertexAttributes());
                }
            }

            private void initializeDepthShaders() {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.getBlending() == BasicShader.Blending.opaque)
                        shaderGroup.initializeDepthShader();
                }
            }

            public boolean needsDepth(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingColorTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                GraphModelsImpl models = pipelineRenderingContext.getGraphShaderModels();

                boolean needsToDrawDepth = pipelineRequirements.isRequiringDepthTexture();
                if (needsToDrawDepth)
                    initializeDepthShaders();
                boolean usesDepth = false;
                if (needsDepth(models)) {
                    usesDepth = true;
                    pipelineRequirements.setRequiringDepthTexture();
                }
                boolean needsSceneColor = isRequiringSceneColor(models);

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                int width = currentBuffer.getWidth();
                int height = currentBuffer.getHeight();
                Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                updateCamera(camera, width, height);
                GraphShaderEnvironment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext, null) : null;
                models.prepareForRendering(camera);

                shaderContext.setCamera(camera);
                shaderContext.setGraphShaderEnvironment(environment);
                shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                shaderContext.setRenderWidth(pipelineRenderingContext.getRenderWidth());
                shaderContext.setRenderHeight(pipelineRenderingContext.getRenderHeight());

                if (needsToDrawDepth || usesDepth) {
                    renderPipeline.enrichWithDepthBuffer(currentBuffer);
                    shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                }

                RenderPipelineBuffer sceneColorBuffer = null;
                if (needsSceneColor) {
                    sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                }

                currentBuffer.beginColor();

                // Initialize shaders for this frame
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    colorShader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    colorShader.setEnvironment(environment);
                    if (needsToDrawDepth) {
                        GraphShader depthShader = shaderGroup.getDepthShader();
                        if (depthShader != null) {
                            depthShader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                            depthShader.setEnvironment(environment);
                        }
                    }
                }

                // First render opaque models
                models.orderFrontToBack();
                for (ShaderGroup shaderGroup : shaderGroups) {
                    ModelGraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.getBlending() == BasicShader.Blending.opaque) {
                        String tag = colorShader.getTag();
                        renderWithShaderOpaquePass(tag, colorShader, models, shaderContext, pipelineRenderingContext.getRenderContext());
                    }
                }

                // Then if depth buffer is needed for later passes - draw to it
                if (needsToDrawDepth) {
                    currentBuffer.endColor();
                    currentBuffer.beginDepth();

                    models.orderFrontToBack();
                    for (ShaderGroup shaderGroup : shaderGroups) {
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
                models.orderBackToFront();
                GraphShader lastShader = null;
                for (IGraphModelInstance graphModelInstance : models.getModels()) {
                    for (ShaderGroup shaderGroup : shaderGroups) {
                        ModelGraphShader colorShader = shaderGroup.getColorShader();
                        if (colorShader.getBlending() != BasicShader.Blending.opaque) {
                            String tag = colorShader.getTag();
                            if (graphModelInstance.hasTag(tag)) {
                                if (lastShader != colorShader) {
                                    if (lastShader != null)
                                        lastShader.end();
                                    colorShader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                                }
                                colorShader.render(shaderContext, graphModelInstance);
                                lastShader = colorShader;
                            }
                        }
                    }
                }
                if (lastShader != null)
                    lastShader.end();

                if (sceneColorBuffer != null)
                    renderPipeline.returnFrameBuffer(sceneColorBuffer);

                currentBuffer.endColor();

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

            private void updateCamera(final Camera camera, final int width, final int height) {
                float viewportWidth = camera.viewportWidth;
                float viewportHeight = camera.viewportHeight;
                if (width != viewportWidth || height != viewportHeight) {
                    camera.viewportWidth = width;
                    camera.viewportHeight = height;
                    camera.update();
                }
            }

            private void renderWithShaderOpaquePass(String tag, ModelGraphShader shader, GraphModelsImpl models, ModelShaderContextImpl shaderContext,
                                                    RenderContext renderContext) {
                boolean begun = false;
                for (IGraphModelInstance graphModelInstance : models.getModels()) {
                    if (graphModelInstance.hasTag(tag)) {
                        if (!begun) {
                            shader.begin(shaderContext, renderContext);
                            begun = true;
                        }
                        shader.render(shaderContext, graphModelInstance);
                    }
                }
                if (begun)
                    shader.end();
            }

            @Override
            public void dispose() {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    shaderGroup.dispose();
                }
                whitePixel.dispose();
            }
        };
    }

    private static ModelGraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        ModelGraphShader modelGraphShader = GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(defaultTexture, false, configurations));
        modelGraphShader.setTag(tag);
        return modelGraphShader;
    }

    private static ModelGraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        ModelGraphShader modelGraphShader = GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(defaultTexture, true, configurations));
        modelGraphShader.setTag(tag);
        return modelGraphShader;
    }

    private class ShaderGroup implements Disposable {
        private JsonValue shaderDefinition;
        private WhitePixel whitePixel;
        private ModelGraphShader colorShader;
        private ModelGraphShader depthShader;

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

        @Override
        public void dispose() {
            colorShader.dispose();
            if (depthShader != null)
                depthShader.dispose();
        }
    }
}
