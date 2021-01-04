package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutput;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.loader.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.shader.model.GraphModels;
import com.gempukku.libgdx.graph.shader.model.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectImpl;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectsImpl;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.screen.GraphScreenShaders;
import com.gempukku.libgdx.graph.shader.screen.GraphScreenShadersImpl;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;

public class PipelineRendererImpl implements PipelineRenderer {
    private TimeProvider timeProvider;
    private Iterable<PipelineNode> nodes;
    private ObjectMap<String, WritablePipelineProperty> pipelinePropertyMap;
    private EndPipelineNode endNode;
    private PipelineRenderingContextImpl pipelineRenderingContext;

    public PipelineRendererImpl(TimeProvider timeProvider, Iterable<PipelineNode> nodes, ObjectMap<String, WritablePipelineProperty> pipelinePropertyMap, EndPipelineNode endNode) {
        this.timeProvider = timeProvider;
        this.nodes = nodes;
        this.pipelinePropertyMap = pipelinePropertyMap;
        this.endNode = endNode;
        pipelineRenderingContext = new PipelineRenderingContextImpl();

        for (PipelineNode node : nodes) {
            node.initializePipeline(pipelineRenderingContext);
        }
    }

    @Override
    public void setPipelineProperty(String property, Object value) {
        WritablePipelineProperty propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        FieldType fieldType = propertyValue.getType();
        if (!fieldType.accepts(value))
            throw new IllegalArgumentException("Property value not accepted, property: " + property);
        propertyValue.setValue(fieldType.convert(value));
    }

    @Override
    public boolean hasPipelineProperty(String property) {
        return pipelinePropertyMap.containsKey(property);
    }

    @Override
    public void unsetPipelineProperty(String property) {
        WritablePipelineProperty propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        propertyValue.unsetValue();
    }

    @Override
    public PipelineProperty getPipelineProperty(String property) {
        return pipelinePropertyMap.get(property);
    }

    @Override
    public Iterable<? extends PipelineProperty> getProperties() {
        return pipelinePropertyMap.values();
    }

    @Override
    public GraphModels getGraphShaderModels() {
        return pipelineRenderingContext.getGraphShaderModels();
    }

    @Override
    public GraphSprites getGraphSprites() {
        return pipelineRenderingContext.getGraphSprites();
    }

    @Override
    public GraphScreenShaders getGraphScreenShaders() {
        return pipelineRenderingContext.getScreenShaders();
    }


    @Override
    public GraphParticleEffects getGraphParticleEffects() {
        return pipelineRenderingContext.getGraphParticleEffects();
    }

    @Override
    public void render(final RenderOutput renderOutput) {
        pipelineRenderingContext.setRenderOutput(renderOutput);

        for (PipelineNode node : nodes) {
            node.startFrame();
        }

        pipelineRenderingContext.update();

        pipelineRenderingContext.getRenderContext().begin();
        RenderPipeline renderPipeline = endNode.executePipeline(pipelineRenderingContext);
        renderOutput.output(renderPipeline, pipelineRenderingContext);
        pipelineRenderingContext.getRenderContext().end();

        for (PipelineNode node : nodes) {
            node.endFrame();
        }
    }

    @Override
    public void dispose() {
        for (PipelineNode node : nodes) {
            node.dispose();
        }
        pipelineRenderingContext.dispose();
    }

    private class PipelineRenderingContextImpl implements PipelineRenderingContext, PipelineInitializationFeedback, Disposable {
        private RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        private RenderOutput renderOutput;
        private FullScreenRenderImpl fullScreenRender = new FullScreenRenderImpl();

        private GraphModelsImpl graphShaderModels = new GraphModelsImpl();
        private GraphScreenShadersImpl screenShaders = new GraphScreenShadersImpl();
        private GraphParticleEffectsImpl particleEffects = new GraphParticleEffectsImpl();
        private GraphSpritesImpl graphSprites = new GraphSpritesImpl();

        public void setRenderOutput(RenderOutput renderOutput) {
            this.renderOutput = renderOutput;
        }

        public void update() {
            particleEffects.setTimeProvider(timeProvider);
            for (GraphParticleEffectImpl particleEffect : particleEffects.getParticleEffects()) {
                particleEffect.generateParticles(timeProvider);
            }
        }

        @Override
        public void registerModelVertexAttributes(String tag, VertexAttributes vertexAttributes) {
            graphShaderModels.registerVertexAttributes(tag, vertexAttributes);
        }

        @Override
        public void registerSpriteShader(String tag, SpriteGraphShader shader) {
            graphSprites.registerTag(tag, shader);
        }

        @Override
        public GraphSpritesImpl getGraphSprites() {
            return graphSprites;
        }

        @Override
        public void registerScreenShader(String tag, PropertyContainerImpl propertyContainer) {
            screenShaders.setPropertyContainer(tag, propertyContainer);
        }

        @Override
        public void registerParticleEffect(String tag, VertexAttributes vertexAttributes, int maxNumberOfParticles, int initialParticles, float particlesPerSecond) {
            particleEffects.registerEffect(tag, vertexAttributes, maxNumberOfParticles, initialParticles, particlesPerSecond);
        }

        @Override
        public int getRenderWidth() {
            return renderOutput.getRenderWidth();
        }

        @Override
        public int getRenderHeight() {
            return renderOutput.getRenderHeight();
        }

        @Override
        public GraphModelsImpl getGraphShaderModels() {
            return graphShaderModels;
        }

        @Override
        public GraphScreenShadersImpl getScreenShaders() {
            return screenShaders;
        }

        @Override
        public GraphParticleEffectsImpl getGraphParticleEffects() {
            return particleEffects;
        }

        @Override
        public PipelinePropertySource getPipelinePropertySource() {
            return PipelineRendererImpl.this;
        }

        @Override
        public TimeProvider getTimeProvider() {
            return timeProvider;
        }

        @Override
        public RenderContext getRenderContext() {
            return renderContext;
        }

        @Override
        public FullScreenRender getFullScreenRender() {
            return fullScreenRender;
        }

        @Override
        public void dispose() {
            graphShaderModels.dispose();
            fullScreenRender.dispose();
            particleEffects.dispose();
        }
    }
}
