package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class PipelineRendererNodeProducer extends PipelineNodeProducerImpl {
    public PipelineRendererNodeProducer() {
        super(new PipelineRendererNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");
        final PipelineNode.FieldOutput<RenderPipeline> otherPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("pipeline");
        final PipelineNode.FieldOutput<Vector2> positionInput = (PipelineNode.FieldOutput<Vector2>) inputFields.get("position");
        final PipelineNode.FieldOutput<Vector2> sizeInput = (PipelineNode.FieldOutput<Vector2>) inputFields.get("size");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private PipelineRequirements paintPipelineRequirement = new PipelineRequirements();

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline canvasPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);
                paintPipelineRequirement.reset();
                RenderPipeline paintPipeline = otherPipelineInput.getValue(pipelineRenderingContext, paintPipelineRequirement);

                RenderPipelineBuffer canvasBuffer = canvasPipeline.getDefaultBuffer();
                RenderPipelineBuffer paintBuffer = paintPipeline.getDefaultBuffer();

                Vector2 position = positionInput.getValue(pipelineRenderingContext, null);
                Vector2 size;
                if (sizeInput != null)
                    size = sizeInput.getValue(pipelineRenderingContext, null);
                else
                    size = new Vector2(paintBuffer.getWidth(), paintBuffer.getHeight());

                canvasPipeline.drawTexture(paintBuffer, canvasBuffer, position.x, position.y, size.x, size.y);

                paintPipeline.returnFrameBuffer(paintBuffer);

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(canvasPipeline);
            }
        };
    }
}
