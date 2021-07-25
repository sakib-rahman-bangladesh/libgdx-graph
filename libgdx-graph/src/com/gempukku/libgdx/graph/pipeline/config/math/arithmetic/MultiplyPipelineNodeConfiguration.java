package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class MultiplyPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public MultiplyPipelineNodeConfiguration() {
        super("Multiply", "Multiply", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputs", "Inputs", true, false, true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction<PipelineFieldType>(Float, "inputs"),
                        Float, Vector2, Vector3, Color));
    }
}
