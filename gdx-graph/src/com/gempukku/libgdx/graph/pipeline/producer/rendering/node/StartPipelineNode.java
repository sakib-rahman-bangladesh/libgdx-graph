package com.gempukku.libgdx.graph.pipeline.producer.rendering.node;

import com.badlogic.gdx.graphics.Color;
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
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class StartPipelineNode extends OncePerFrameJobPipelineNode {
    private FieldOutput<Color> color;
    private FieldOutput<Vector2> size;

    private RenderPipelineImpl renderPipeline;

    public StartPipelineNode(NodeConfiguration configuration, ObjectMap<String, FieldOutput<?>> inputFields) {
        super(configuration, inputFields);
        color = (FieldOutput<Color>) inputFields.get("background");
        size = (FieldOutput<Vector2>) inputFields.get("size");
    }

    @Override
    public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {
        renderPipeline = new RenderPipelineImpl(pipelineInitializationFeedback.getBufferCopyHelper(),
                pipelineInitializationFeedback.getTextureBufferCache());
    }

    @Override
    protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
        float bufferX = (size != null) ? size.getValue(pipelineRenderingContext, null).x : pipelineRenderingContext.getRenderWidth();
        float bufferY = (size != null) ? size.getValue(pipelineRenderingContext, null).y : pipelineRenderingContext.getRenderHeight();

        Color backgroundColor = (color != null) ? color.getValue(pipelineRenderingContext, null) : Color.BLACK;

        int width = MathUtils.round(bufferX);
        int height = MathUtils.round(bufferY);

        RenderPipelineBuffer frameBuffer = renderPipeline.initializeDefaultBuffer(width, height, Pixmap.Format.RGB888, backgroundColor);
        // Dummy call to make sure frame buffer is drawn
        frameBuffer.beginColor();
        frameBuffer.endColor();

        OutputValue<RenderPipeline> output = outputValues.get("output");
        if (output != null)
            output.setValue(renderPipeline);
    }
}
