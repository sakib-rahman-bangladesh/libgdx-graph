package com.gempukku.libgdx.graph.pipeline.config.math;

import com.gempukku.libgdx.graph.config.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector3;

public class MultiplyPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public MultiplyPipelineNodeConfiguration() {
        super("Multiply", "Multiply", "Math");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputs", "Inputs", true, false, true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction<PipelineFieldType>(Float, "inputs"),
                        Float, Vector2, Vector3, Color));
    }
}
