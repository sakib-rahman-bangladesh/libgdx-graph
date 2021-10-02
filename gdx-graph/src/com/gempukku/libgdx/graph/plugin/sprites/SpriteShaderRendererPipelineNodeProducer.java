package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.sprites.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class SpriteShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new SpriteShaderConfiguration()};
    private PluginPrivateDataSource pluginPrivateDataSource;

    public SpriteShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new SpriteShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ShaderContextImpl shaderContext = new ShaderContextImpl(pluginPrivateDataSource);

        final Array<SpriteGraphShader> opaqueShaders = new Array<>();
        final Array<SpriteGraphShader> translucentShaders = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            SpriteGraphShader shader = createColorShader(shaderDefinition, whitePixel.texture);
            if (shader.getBlending().getDepthMask())
                opaqueShaders.add(shader);
            else
                translucentShaders.add(shader);
        }

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                GraphSpritesImpl graphSprites = pipelineInitializationFeedback.getPrivatePluginData(GraphSpritesImpl.class);
                for (SpriteGraphShader shader : opaqueShaders) {
                    graphSprites.registerTag(shader.getTag(), shader);
                }
                for (SpriteGraphShader shader : translucentShaders) {
                    graphSprites.registerTag(shader.getTag(), shader);
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

                GraphSpritesImpl sprites = pipelineRenderingContext.getPrivatePluginData(GraphSpritesImpl.class);
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

                    shaderContext.setCamera(camera);
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
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        SpriteGraphShader spriteGraphShader = GraphLoader.loadGraph(shaderGraph, new SpriteShaderLoaderCallback(defaultTexture, configurations), PropertyLocation.Attribute);
        spriteGraphShader.setTag(tag);
        return spriteGraphShader;
    }
}
