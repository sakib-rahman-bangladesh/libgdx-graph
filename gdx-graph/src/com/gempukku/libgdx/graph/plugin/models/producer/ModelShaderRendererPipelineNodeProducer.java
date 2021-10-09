package com.gempukku.libgdx.graph.plugin.models.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.*;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderLoaderCallback;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.strategy.*;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.util.WhitePixel;

import java.util.function.Function;

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

        final ObjectMap<String, ShaderGroup> shaderGroups = new ObjectMap<>();

        final Array<String> allShaderTags = new Array<>();
        final Array<String> depthDrawingShaderTags = new Array<>();

        final JsonValue shaderDefinitions = data.get("shaders");
        for (JsonValue shaderDefinition : shaderDefinitions) {
            ShaderGroup shaderGroup = new ShaderGroup(shaderDefinition, whitePixel);
            shaderGroup.initialize();

            allShaderTags.add(shaderGroup.getTag());
            shaderGroups.put(shaderGroup.getTag(), shaderGroup);

            if (shaderGroup.getColorShader().getBlending().getDepthMask())
                depthDrawingShaderTags.add(shaderGroup.getTag());
        }

        RenderOrder renderOrder = RenderOrder.valueOf(data.getString("renderOrder", "Shader_Unordered"));
        final ModelRenderingStrategy renderingStrategy = createRenderingStrategy(renderOrder);

        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final RenderingStrategyCallback colorStrategyCallback = new RenderingStrategyCallback(
                shaderContext, new Function<String, ModelGraphShader>() {
            @Override
            public ModelGraphShader apply(String s) {
                return shaderGroups.get(s).getColorShader();
            }
        });

        final RenderingStrategyCallback depthStrategyCallback = new RenderingStrategyCallback(
                shaderContext, new Function<String, ModelGraphShader>() {
            @Override
            public ModelGraphShader apply(String s) {
                return shaderGroups.get(s).getDepthShader();
            }
        });

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
                GraphModelsImpl graphModels = pipelineInitializationFeedback.getPrivatePluginData(GraphModelsImpl.class);
                for (ShaderGroup shaderGroup : shaderGroups.values()) {
                    ModelGraphShader shader = shaderGroup.getColorShader();
                    graphModels.registerTag(shader.getTag(), shader);
                }
            }

            private void initializeDepthShaders() {
                for (String depthDrawingShaderTag : depthDrawingShaderTags) {
                    shaderGroups.get(depthDrawingShaderTag).initializeDepthShader();
                }
            }

            public boolean needsDepth(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups.values()) {
                    GraphShader colorShader = shaderGroup.getColorShader();
                    if (colorShader.isUsingDepthTexture() && graphShaderModels.hasModelWithTag(colorShader.getTag()))
                        return true;
                }
                return false;
            }

            public boolean isRequiringSceneColor(GraphModelsImpl graphShaderModels) {
                for (ShaderGroup shaderGroup : shaderGroups.values()) {
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
                    if (needsSceneColor)
                        sceneColorBuffer = setupColorTexture(renderPipeline, currentBuffer, pipelineRenderingContext);

                    // Drawing models on color buffer
                    colorStrategyCallback.prepare(pipelineRenderingContext, models);
                    currentBuffer.beginColor();
                    renderingStrategy.processModels(models, allShaderTags, camera, colorStrategyCallback);
                    currentBuffer.endColor();

                    if (needsToDrawDepth) {
                        // Drawing models on depth buffer
                        depthStrategyCallback.prepare(pipelineRenderingContext, models);
                        currentBuffer.beginDepth();
                        renderingStrategy.processModels(models, depthDrawingShaderTags, camera, depthStrategyCallback);
                        currentBuffer.endDepth();
                    }

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
                for (ShaderGroup shaderGroup : shaderGroups.values()) {
                    shaderGroup.dispose();
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

    private static ModelGraphShader createColorShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(tag, defaultTexture, false, configurations), PropertyLocation.Uniform);
    }

    private static ModelGraphShader createDepthShader(JsonValue shaderDefinition, Texture defaultTexture) {
        JsonValue shaderGraph = shaderDefinition.get("shader");
        String tag = shaderDefinition.getString("tag");
        Gdx.app.debug("Shader", "Building shader with tag: " + tag);
        return GraphLoader.loadGraph(shaderGraph, new ModelShaderLoaderCallback(tag, defaultTexture, true, configurations), PropertyLocation.Uniform);
    }

    private static class ShaderGroup implements Disposable {
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
            if (depthShader == null && colorShader.getBlending().getDepthMask()) {
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

                shaderContext.setGlobalPropertyContainer(graphModels.getGlobalProperties(tag));
                shader.begin(shaderContext, context.getRenderContext());
                runningShader = shader;
            }
            shader.render(shaderContext, graphModel);
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
