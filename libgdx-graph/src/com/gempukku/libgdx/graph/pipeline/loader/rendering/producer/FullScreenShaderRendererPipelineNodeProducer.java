package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.FullScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.FullScreenShaderConfiguration;
import com.gempukku.libgdx.graph.shader.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public class FullScreenShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new GraphShaderConfiguration(), new FullScreenShaderConfiguration()};

    public FullScreenShaderRendererPipelineNodeProducer() {
        super(new FullScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl();

        JsonValue shaderJson = data.get("shader");
        final GraphShader graphShader = GraphLoader.loadGraph(shaderJson, new ShaderLoaderCallback(whitePixel.texture, true, configurations));
        graphShader.setCulling(BasicShader.Culling.back);
        graphShader.setDepthTesting(BasicShader.DepthTesting.disabled);
        graphShader.setTransparency(BasicShader.Transparency.transparent);

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<GraphShaderEnvironment> lightsInput = (PipelineNode.FieldOutput<GraphShaderEnvironment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                boolean usesDepth = false;

                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);
                if (enabled) {
                    if (graphShader.isUsingDepthTexture()) {
                        usesDepth = true;
                        pipelineRequirements.setRequiringDepthTexture();
                    }
                }

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                if (enabled) {
                    boolean needsSceneColor = graphShader.isUsingColorTexture();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    if (usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                    }

                    if (cameraInput != null) {
                        Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                        int width = currentBuffer.getWidth();
                        int height = currentBuffer.getHeight();
                        updateCamera(camera, width, height);
                        shaderContext.setCamera(camera);
                    }
                    GraphShaderEnvironment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext, null) : null;
                    shaderContext.setGraphShaderEnvironment(environment);

                    shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    shaderContext.setRenderWidth(pipelineRenderingContext.getRenderWidth());
                    shaderContext.setRenderHeight(pipelineRenderingContext.getRenderHeight());

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    currentBuffer.beginColor();

                    graphShader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                    graphShader.render(shaderContext, pipelineRenderingContext.getFullScreenRender());
                    graphShader.end();

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

            private void updateCamera(final Camera camera, final int width, final int height) {
                float viewportWidth = camera.viewportWidth;
                float viewportHeight = camera.viewportHeight;
                if (width != viewportWidth || height != viewportHeight) {
                    camera.viewportWidth = width;
                    camera.viewportHeight = height;
                    camera.update();
                }
            }

            @Override
            public void dispose() {
                graphShader.dispose();
                whitePixel.dispose();
            }
        };
    }
}
