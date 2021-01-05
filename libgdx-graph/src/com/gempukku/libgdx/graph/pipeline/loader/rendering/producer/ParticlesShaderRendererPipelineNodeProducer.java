package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsUniformShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectImpl;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectsImpl;
import com.gempukku.libgdx.graph.shader.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.shader.particles.ParticlesShaderConfiguration;
import com.gempukku.libgdx.graph.shader.particles.ParticlesShaderLoaderCallback;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ParticlesShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ParticlesShaderConfiguration()};

    public ParticlesShaderRendererPipelineNodeProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl();

        final Array<ParticlesGraphShader> particleShaders = new Array<>();
        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            String tag = shaderDefinition.getString("tag");
            JsonValue shaderGraph = shaderDefinition.get("shader");
            final ParticlesGraphShader graphShader = GraphLoader.loadGraph(shaderGraph, new ParticlesShaderLoaderCallback(whitePixel.texture, configurations));
            graphShader.setTag(tag);
            particleShaders.add(graphShader);
        }

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<GraphShaderEnvironment> lightsInput = (PipelineNode.FieldOutput<GraphShaderEnvironment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                for (ParticlesGraphShader particleShader : particleShaders) {
                    pipelineInitializationFeedback.registerParticleEffectShader(particleShader.getTag(), particleShader);
                }
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                boolean usesDepth = false;

                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);
                if (enabled) {
                    for (ParticlesGraphShader particleShader : particleShaders) {
                        if (particleShader.isUsingDepthTexture()) {
                            usesDepth = true;
                            pipelineRequirements.setRequiringDepthTexture();
                            break;
                        }
                    }
                }

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                if (enabled) {
                    boolean needsSceneColor = false;
                    for (ParticlesGraphShader particleShader : particleShaders) {
                        if (particleShader.isUsingColorTexture()) {
                            needsSceneColor = true;
                            break;
                        }
                    }

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    if (usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                    }

                    Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                    int width = currentBuffer.getWidth();
                    int height = currentBuffer.getHeight();
                    updateCamera(camera, width, height);
                    shaderContext.setCamera(camera);

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

                    GraphParticleEffectsImpl particleEffects = pipelineRenderingContext.getGraphParticleEffects();
                    for (ParticlesGraphShader particleShader : particleShaders) {
                        particleShader.begin(shaderContext, pipelineRenderingContext.getRenderContext());
                        String tag = particleShader.getTag();
                        for (GraphParticleEffectImpl particleEffect : particleEffects.getParticleEffects()) {
                            if (particleEffect.getTag().equals(tag)) {
                                shaderContext.setPropertyContainer(particleEffect.getPropertyContainer());
                                particleEffect.render(particleShader, shaderContext);
                            }
                        }
                        particleShader.end();
                    }

                    currentBuffer.endColor();

                    if (sceneColorBuffer != null)
                        renderPipeline.returnFrameBuffer(sceneColorBuffer);
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
                for (ParticlesGraphShader particleShader : particleShaders) {
                    particleShader.dispose();
                }
                whitePixel.dispose();
            }
        };
    }
}
