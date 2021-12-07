package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderLoaderCallback;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.models.strategy.*;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.util.WhitePixel;

import java.util.function.Function;

public class ShadowShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    private static GraphConfiguration[] configurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ModelShaderConfiguration()};
    private PluginPrivateDataSource pluginPrivateDataSource;

    public ShadowShaderRendererPipelineNodeProducer(PluginPrivateDataSource pluginPrivateDataSource) {
        super(new ShadowShaderRendererPipelineNodeConfiguration());
        this.pluginPrivateDataSource = pluginPrivateDataSource;
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final WhitePixel whitePixel = new WhitePixel();

        final ModelShaderContextImpl shaderContext = new ModelShaderContextImpl(pluginPrivateDataSource);

        final ObjectMap<String, ModelGraphShader> shaders = new ObjectMap<>();
        final Array<String> allShaderTags = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            ModelGraphShader depthGraphShader = ShadowShaderRendererPipelineNodeProducer.createDepthShader(shaderDefinition, whitePixel.texture);

            allShaderTags.add(depthGraphShader.getTag());
            shaders.put(depthGraphShader.getTag(), depthGraphShader);
        }

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final ModelRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final String environmentId = data.getString("id", "");

        final RenderingStrategyCallback depthStrategyCallback = new RenderingStrategyCallback(
                shaderContext, new Function<String, ModelGraphShader>() {
            @Override
            public ModelGraphShader apply(String s) {
                return shaders.get(s);
            }
        });

        final Array<RenderPipelineBuffer> createdPipelineBuffers = new Array<>();

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private RenderPipeline pipeline;

            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                GraphModelsImpl graphModels = pipelineInitializationFeedback.getPrivatePluginData(GraphModelsImpl.class);
                for (ObjectMap.Entry<String, ModelGraphShader> shaderEntry : shaders.entries()) {
                    graphModels.registerTag(shaderEntry.key, shaderEntry.value);
                }
            }

            public boolean needsDepth(GraphModelsImpl graphShaderModels) {
                for (ModelGraphShader shader : shaders.values()) {
                    if (shader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphModelsImpl graphShaderModels) {
                for (ModelGraphShader shader : shaders.values()) {
                    if (shader.isUsingColorTexture() && graphShaderModels.hasModelWithTag(shader.getTag()))
                        return true;
                }
                return false;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);

                GraphModelsImpl models = pipelineRenderingContext.getPrivatePluginData(GraphModelsImpl.class);

                boolean usesDepth = false;
                if (enabled) {
                    if (needsDepth(models)) {
                        usesDepth = true;
                        pipelineRequirements.setRequiringDepthTexture();
                    }
                }

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);
                this.pipeline = renderPipeline;

                if (enabled) {
                    boolean needsDrawing = false;
                    Lighting3DPrivateData lighting = pipelineRenderingContext.getPrivatePluginData(Lighting3DPrivateData.class);
                    Lighting3DEnvironment environment = lighting.getEnvironment(environmentId);
                    // Initialize directional light cameras and textures
                    for (Directional3DLight directionalLight : environment.getDirectionalLights()) {
                        if (directionalLight.isShadowsEnabled()) {
                            needsDrawing = true;
                            directionalLight.updateCamera(environment.getSceneCenter(), environment.getSceneDiameter());
                            if (directionalLight.getShadowFrameBuffer() == null) {
                                RenderPipelineBuffer shadowFrameBuffer = renderPipeline.getNewFrameBuffer(1024, 1024, Pixmap.Format.RGB888, Color.WHITE);
                                directionalLight.setShadowFrameBuffer(shadowFrameBuffer);
                            }
                        }
                    }

                    if (needsDrawing) {
                        boolean needsSceneColor = isRequiringSceneColor(models);

                        RenderPipelineBuffer drawBuffer = renderPipeline.getDefaultBuffer();

                        for (Directional3DLight directionalLight : environment.getDirectionalLights()) {
                            if (directionalLight.isShadowsEnabled()) {
                                RenderPipelineBuffer shadowBuffer = directionalLight.getShadowFrameBuffer();
                                Camera camera = directionalLight.getShadowCamera();

                                shaderContext.setCamera(camera);
                                shaderContext.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                                shaderContext.setRenderWidth(shadowBuffer.getWidth());
                                shaderContext.setRenderHeight(shadowBuffer.getHeight());

                                if (usesDepth) {
                                    renderPipeline.enrichWithDepthBuffer(drawBuffer);
                                    shaderContext.setDepthTexture(drawBuffer.getDepthBufferTexture());
                                }

                                if (needsSceneColor)
                                    shaderContext.setColorTexture(drawBuffer.getColorBufferTexture());

                                // Drawing models on color buffer
                                depthStrategyCallback.prepare(pipelineRenderingContext, models);
                                RenderPipelineBuffer defaultBuffer = pipeline.getDefaultBuffer();
                                //defaultBuffer.beginColor();
                                shadowBuffer.beginColor();
                                renderingStrategy.processModels(models, allShaderTags, camera, depthStrategyCallback);
                                //defaultBuffer.endColor();
                                shadowBuffer.endColor();
                            }
                        }
                    }
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void endFrame() {
                for (RenderPipelineBuffer createdPipelineBuffer : createdPipelineBuffers) {
                    pipeline.returnFrameBuffer(createdPipelineBuffer);
                }
            }

            @Override
            public void dispose() {
                for (ModelGraphShader shader : shaders.values()) {
                    shader.dispose();
                }
                whitePixel.dispose();
            }
        };
    }

    private ModelRenderingStrategy createRenderingStrategy(RenderOrder renderOrder) {
        switch (renderOrder) {
            case Shader_Unordered:
                return new ShaderUnorderedModelRenderingStrategy();
            case Shader_Back_To_Front:
                return new ShaderBackToFrontModelRenderingStrategy();
            case Shader_Front_To_Back:
                return new ShaderFrontToBackModelRenderingStrategy();
            case Back_To_Front:
                return new BackToFrontModelRenderingStrategy();
            case Front_To_Back:
                return new FrontToBackModelRenderingStrategy();
        }
        throw new IllegalStateException("Unrecognized RenderOrder: " + renderOrder.name());
    }

    private static ModelGraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(tag, defaultTexture, true, configurations), PropertyLocation.Uniform);
    }

    private static class RenderingStrategyCallback implements ModelRenderingStrategy.StrategyCallback {
        private ModelShaderContextImpl shaderContext;
        private Function<String, ModelGraphShader> shaderResolver;

        private GraphModelsImpl graphModels;
        private PipelineRenderingContext context;
        private GraphShader runningShader = null;

        public RenderingStrategyCallback(ModelShaderContextImpl shaderContext, Function<String, ModelGraphShader> shaderResolver) {
            this.shaderContext = shaderContext;
            this.shaderResolver = shaderResolver;
        }

        public void prepare(PipelineRenderingContext context, GraphModelsImpl graphModels) {
            this.context = context;
            this.graphModels = graphModels;
        }

        @Override
        public void begin() {

        }

        @Override
        public void process(GraphModel graphModel, String tag) {
            ModelGraphShader shader = shaderResolver.apply(tag);
            if (runningShader != shader) {
                endCurrentShader();
                beginShader(tag, shader);
            }
            shader.render(shaderContext, graphModel);
        }

        private void beginShader(String tag, ModelGraphShader shader) {
            shaderContext.setGlobalPropertyContainer(graphModels.getGlobalProperties(tag));
            shader.begin(shaderContext, context.getRenderContext());
            runningShader = shader;
        }

        private void endCurrentShader() {
            if (runningShader != null)
                runningShader.end();
        }

        @Override
        public void end() {
            endCurrentShader();
            runningShader = null;
        }
    }
}
