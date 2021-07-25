package com.gempukku.libgdx.graph.pipeline.config.math.arithmetic;

import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class SubtractPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public SubtractPipelineNodeConfiguration() {
        super("Subtract", "Subtract", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputA", "A", true,
                        Color, Vector3, Vector2, Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputB", "B", true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        new VectorArithmeticOutputTypeFunction<PipelineFieldType>(Float, "inputA", "inputB"),
                        Float, Vector2, Vector3, Color));
    }
}
