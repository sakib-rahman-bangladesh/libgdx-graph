package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.TimeKeeper;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutput;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.loader.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelsImpl;

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

    private class PipelineRenderingContextImpl implements PipelineRenderingContext, Disposable {
        private RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        private RenderOutput renderOutput;
        private GraphShaderModelsImpl graphShaderModels = new GraphShaderModelsImpl();
        private FullScreenRenderImpl fullScreenRender = new FullScreenRenderImpl();

        public void setRenderOutput(RenderOutput renderOutput) {
            this.renderOutput = renderOutput;
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
        }
    }

    private static class FullScreenRenderImpl implements FullScreenRender, Disposable {
        private VertexBufferObject vertexBufferObject;
        private IndexBufferObject indexBufferObject;

        public FullScreenRenderImpl() {
            float[] verticeData = new float[]{
                    0, 0, 0,
                    0, 1, 0,
                    1, 0, 0,
                    1, 1, 0};
            short[] indices = {0, 2, 1, 2, 3, 1};

            vertexBufferObject = new VertexBufferObject(true, 4, VertexAttribute.Position());
            indexBufferObject = new IndexBufferObject(true, indices.length);
            vertexBufferObject.setVertices(verticeData, 0, verticeData.length);
            indexBufferObject.setIndices(indices, 0, indices.length);
        }

        @Override
        public void renderFullScreen(ShaderProgram shaderProgram) {
            vertexBufferObject.bind(shaderProgram);
            indexBufferObject.bind();
            Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
            vertexBufferObject.unbind(shaderProgram);
            indexBufferObject.unbind();
        }

        @Override
        public void dispose() {
            vertexBufferObject.dispose();
            indexBufferObject.dispose();
        }
    }
}
