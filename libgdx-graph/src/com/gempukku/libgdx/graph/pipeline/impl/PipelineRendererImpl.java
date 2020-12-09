package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.graphics.VertexAttribute;
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
import com.gempukku.libgdx.graph.shader.model.GraphShaderModels;
import com.gempukku.libgdx.graph.shader.model.impl.GraphShaderModelsImpl;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectsImpl;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.screen.ScreenShaders;
import com.gempukku.libgdx.graph.shader.screen.ScreenShadersImpl;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.graph.util.FullScreenRenderImpl;

public class PipelineRendererImpl implements PipelineRenderer {
    private Iterable<PipelineNode> nodes;
    private ObjectMap<String, WritablePipelineProperty> pipelinePropertyMap;
    private EndPipelineNode endNode;
    private TimeKeeper timeKeeper;
    private PipelineRenderingContextImpl pipelineRenderingContext;

    public PipelineRendererImpl(Iterable<PipelineNode> nodes, ObjectMap<String, WritablePipelineProperty> pipelinePropertyMap, EndPipelineNode endNode) {
        this.nodes = nodes;
        this.pipelinePropertyMap = pipelinePropertyMap;
        this.endNode = endNode;
        this.timeKeeper = new DefaultTimeKeeper();
        pipelineRenderingContext = new PipelineRenderingContextImpl();

        for (PipelineNode node : nodes) {
            node.initializePipeline(pipelineRenderingContext);
        }
    }

    @Override
    public void setTimeKeeper(TimeKeeper timeKeeper) {
        this.timeKeeper = timeKeeper;
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
    public GraphShaderModels getGraphShaderModels() {
        return pipelineRenderingContext.getGraphShaderModels();
    }

    @Override
    public ScreenShaders getScreenShaders() {
        return pipelineRenderingContext.getScreenShaders();
    }

    @Override
    public void render(float delta, final RenderOutput renderOutput) {
        timeKeeper.updateTime(delta);
        pipelineRenderingContext.setRenderOutput(renderOutput);

        for (PipelineNode node : nodes) {
            node.startFrame(delta);
        }

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

        private GraphShaderModelsImpl graphShaderModels = new GraphShaderModelsImpl();
        private ScreenShadersImpl screenShaders = new ScreenShadersImpl();
        private GraphParticleEffectsImpl particleEffects = new GraphParticleEffectsImpl();

        public void setRenderOutput(RenderOutput renderOutput) {
            this.renderOutput = renderOutput;
        }

        @Override
        public void registerModelAttribute(VertexAttribute vertexAttribute) {
            graphShaderModels.registerAttribute(vertexAttribute);
        }

        @Override
        public void registerScreenShader(String tag, PropertyContainerImpl propertyContainer) {
            screenShaders.setPropertyContainer(tag, propertyContainer);
        }

        @Override
        public void registerParticleEffect(String tag, int maxNumberOfParticles) {
            particleEffects.registerEffect(tag, maxNumberOfParticles);
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
        public GraphShaderModelsImpl getGraphShaderModels() {
            return graphShaderModels;
        }

        @Override
        public ScreenShadersImpl getScreenShaders() {
            return screenShaders;
        }

        @Override
        public PipelinePropertySource getPipelinePropertySource() {
            return PipelineRendererImpl.this;
        }

        @Override
        public TimeProvider getTimeProvider() {
            return timeKeeper;
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
