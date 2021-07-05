package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsUniformShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ParticlesShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ParticlesShaderConfiguration()};
    private PluginPrivateDataSource pluginPrivateDataSource;

    public ParticlesShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl(pluginPrivateDataSource);

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
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                for (ParticlesGraphShader particleShader : particleShaders) {
                    pipelineInitializationFeedback.getPrivatePluginData(GraphParticleEffectsImpl.class).registerEffect(particleShader.getTag(), particleShader);
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

                    if (cameraInput != null) {
                        Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                        shaderContext.setCamera(camera);
                    }

                    shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    currentBuffer.beginColor();

                    GraphParticleEffectsImpl particleEffects = pipelineRenderingContext.getPrivatePluginData(GraphParticleEffectsImpl.class);
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
