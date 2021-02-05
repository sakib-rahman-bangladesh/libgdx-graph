package com.gempukku.libgdx.graph.pipeline.producer.rendering.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.impl.RenderPipelineImpl;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class StartPipelineNode extends OncePerFrameJobPipelineNode {
    private FieldOutput<Color> color;
    private FieldOutput<Vector2> size;

    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(NodeConfiguration configuration, ObjectMap<String, FieldOutput<?>> inputFields) {
        super(configuration, inputFields);
        color = (FieldOutput<Color>) inputFields.get("background");
        size = (FieldOutput<Vector2>) inputFields.get("size");

        renderPipeline = new RenderPipelineImpl();
    }

    @Override
    protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
        float bufferX = (size != null) ? size.getValue(pipelineRenderingContext, null).x : pipelineRenderingContext.getRenderWidth();
        float bufferY = (size != null) ? size.getValue(pipelineRenderingContext, null).y : pipelineRenderingContext.getRenderHeight();

        Color backgroundColor = (color != null) ? color.getValue(pipelineRenderingContext, null) : Color.BLACK;

        int width = MathUtils.round(bufferX);
        int height = MathUtils.round(bufferY);
        renderPipeline.startFrame();

        RenderPipelineBuffer frameBuffer = renderPipeline.initializeDefaultBuffer(width, height, Pixmap.Format.RGB888);
        frameBuffer.beginColor();
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
        frameBuffer.endColor();

        OutputValue<RenderPipeline> output = outputValues.get("output");
        if (output != null)
            output.setValue(renderPipeline);
    }

    @Override
    public void endFrame() {
        renderPipeline.endFrame();
        super.endFrame();
    }

    @Override
    public void dispose() {
        renderPipeline.dispose();
    }
}
