package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelsImpl;

public class GraphShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GraphShaderRendererPipelineNodeProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl();

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
            private final RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));

            private void initializeDepthShaders() {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.getTransparency() == BasicShader.Transparency.opaque)
                        shaderGroup.initializeDepthShader();
                }
            }

            public boolean needsDepth(GraphShaderModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphShaderModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingColorTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                GraphShaderModelsImpl models = pipelineRenderingContext.getGraphShaderModels();

                boolean usesDepth = false;
                boolean needsToDrawDepth = pipelineRequirements.isRequiringDepthTexture();
                if (needsToDrawDepth)
                    initializeDepthShaders();
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

                if (needsToDrawDepth || usesDepth) {
                    renderPipeline.enrichWithDepthBuffer(currentBuffer);
                    shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                }

                RenderPipelineBuffer sceneColorBuffer = null;
                if (needsSceneColor) {
                    sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer);
                }

                renderContext.begin();

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
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.getTransparency() == BasicShader.Transparency.opaque) {
                        String tag = colorShader.getTag();
                        renderWithShaderOpaquePass(tag, colorShader, models, shaderContext);
                    }
                }

                // Then if depth buffer is needed for later passes - draw to it
                if (needsToDrawDepth) {
                    currentBuffer.endColor();
                    currentBuffer.beginDepth();

                    models.orderFrontToBack();
                    for (ShaderGroup shaderGroup : shaderGroups) {
                        GraphShader depthShader = shaderGroup.getDepthShader();
                        String tag = depthShader.getTag();
                        renderWithShaderOpaquePass(tag, depthShader, models, shaderContext);
                    }

                    currentBuffer.endDepth();
                    currentBuffer.beginColor();
                }

                // Then render transparent models
                models.orderBackToFront();
                GraphShader lastShader = null;
                for (GraphShaderModelInstance graphShaderModelInstance : models.getModels()) {
                    for (ShaderGroup shaderGroup : shaderGroups) {
                        GraphShader colorShader = shaderGroup.getColorShader();
                        if (colorShader.getTransparency() == BasicShader.Transparency.transparent) {
                            String tag = colorShader.getTag();
                            if (graphShaderModelInstance.hasTag(tag)) {
                                if (lastShader != colorShader) {
                                    if (lastShader != null)
                                        lastShader.end();
                                    colorShader.begin(shaderContext, renderContext);
                                }
                                colorShader.render(shaderContext, graphShaderModelInstance);
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
                renderContext.end();
                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private RenderPipelineBuffer setupColorTexture(final RenderPipeline renderPipeline, final RenderPipelineBuffer currentBuffer) {
                RenderPipelineBuffer sceneColorBuffer;
                sceneColorBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);
                sceneColorBuffer.beginColor();
                Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                sceneColorBuffer.endColor();
                shaderContext.setColorTexture(sceneColorBuffer.getColorBufferTexture());
                renderPipeline.getBufferCopyHelper().copy(currentBuffer.getColorBuffer(), sceneColorBuffer.getColorBuffer());
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

            private void renderWithShaderOpaquePass(String tag, GraphShader shader, GraphShaderModelsImpl models, ShaderContext shaderContext) {
                boolean begun = false;
                for (GraphShaderModelInstance graphShaderModelInstance : models.getModels()) {
                    if (graphShaderModelInstance.hasTag(tag)) {
                        if (!begun) {
                            shader.begin(shaderContext, renderContext);
                            begun = true;
                        }
                        shader.render(shaderContext, graphShaderModelInstance);
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

    private static GraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback(defaultTexture));
    }

    private static GraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback(defaultTexture, true));
    }

    private class ShaderGroup implements Disposable {
        private JsonValue shaderDefinition;
        private WhitePixel whitePixel;
        private final String tag;
        private GraphShader colorShader;
        private GraphShader depthShader;

        public ShaderGroup(JsonValue shaderDefinition, WhitePixel whitePixel) {
            this.shaderDefinition = shaderDefinition;
            this.whitePixel = whitePixel;
            this.tag = shaderDefinition.getString("tag");
        }

        public void initialize() {
            colorShader = GraphShaderRendererPipelineNodeProducer.createColorShader(shaderDefinition, whitePixel.texture);
            colorShader.setTag(tag);
        }

        public void initializeDepthShader() {
            if (depthShader == null && colorShader.getTransparency() == BasicShader.Transparency.opaque) {
                depthShader = GraphShaderRendererPipelineNodeProducer.createDepthShader(shaderDefinition, whitePixel.texture);
                depthShader.setTag(tag);
            }
        }

        public GraphShader getColorShader() {
            return colorShader;
        }

        public GraphShader getDepthShader() {
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
