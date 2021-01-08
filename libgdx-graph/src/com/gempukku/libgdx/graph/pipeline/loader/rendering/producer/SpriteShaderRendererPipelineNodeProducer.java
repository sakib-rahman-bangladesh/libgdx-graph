package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.SpriteShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsAttributeShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteShaderConfiguration;
import com.gempukku.libgdx.graph.shader.sprite.SpriteShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.sprite.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class SpriteShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsAttributeShaderConfiguration(), new SpriteShaderConfiguration()};

    public SpriteShaderRendererPipelineNodeProducer() {
        super(new SpriteShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ModelShaderContextImpl shaderContext = new ModelShaderContextImpl();

        final Array<SpriteGraphShader> opaqueShaders = new Array<>();
        final Array<SpriteGraphShader> translucentShaders = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            SpriteGraphShader shader = createColorShader(shaderDefinition, whitePixel.texture);
            if (shader.getBlending() == BasicShader.Blending.opaque)
                opaqueShaders.add(shader);
            else
                translucentShaders.add(shader);
        }

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<GraphShaderEnvironment> lightsInput = (PipelineNode.FieldOutput<GraphShaderEnvironment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                for (SpriteGraphShader shader : opaqueShaders) {
                    pipelineInitializationFeedback.registerSpriteShader(shader.getTag(), shader);
                }
                for (SpriteGraphShader shader : translucentShaders) {
                    pipelineInitializationFeedback.registerSpriteShader(shader.getTag(), shader);
                }
            }

            public boolean needsDepth(GraphSpritesImpl graphSprites) {
                for (SpriteGraphShader shader : opaqueShaders) {
                    if (shader.isUsingDepthTexture() && graphSprites.hasSpriteWithTag(shader.getTag()))
                        return true;
                }
                for (SpriteGraphShader shader : translucentShaders) {
                    if (shader.isUsingDepthTexture() && graphSprites.hasSpriteWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphSpritesImpl graphSprites) {
                for (SpriteGraphShader shader : opaqueShaders) {
                    if (shader.isUsingColorTexture() && graphSprites.hasSpriteWithTag(shader.getTag()))
                        return true;
                }
                for (SpriteGraphShader shader : translucentShaders) {
                    if (shader.isUsingColorTexture() && graphSprites.hasSpriteWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);

                GraphSpritesImpl sprites = pipelineRenderingContext.getGraphSprites();
                boolean usesDepth = false;
                if (enabled) {
                    if (needsDepth(sprites)) {
                        usesDepth = true;
                        pipelineRequirements.setRequiringDepthTexture();
                    }
                }
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                if (enabled) {
                    boolean needsSceneColor = isRequiringSceneColor(sprites);

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                    Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                    GraphShaderEnvironment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext, null) : null;

                    shaderContext.setCamera(camera);
                    shaderContext.setGraphShaderEnvironment(environment);
                    shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    shaderContext.setRenderWidth(currentBuffer.getWidth());
                    shaderContext.setRenderHeight(currentBuffer.getHeight());

                    if (usesDepth) {
                        renderPipeline.enrichWithDepthBuffer(currentBuffer);
                        shaderContext.setDepthTexture(currentBuffer.getDepthBufferTexture());
                    }

                    RenderPipelineBuffer sceneColorBuffer = null;
                    if (needsSceneColor) {
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);
                    }

                    currentBuffer.beginColor();

                    sprites.render(shaderContext, pipelineRenderingContext.getRenderContext(), opaqueShaders, translucentShaders);

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

            @Override
            public void dispose() {
                for (SpriteGraphShader shader : opaqueShaders) {
                    shader.dispose();
                }
                for (SpriteGraphShader shader : translucentShaders) {
                    shader.dispose();
                }
                whitePixel.dispose();
            }
        };
    }

    private static SpriteGraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        SpriteGraphShader spriteGraphShader = GraphLoader.loadGraph(shaderGraph, new SpriteShaderLoaderCallback(defaultTexture, configurations));
        spriteGraphShader.setTag(tag);
        return spriteGraphShader;
    }
}
