package com.gempukku.libgdx.graph.pipeline.config.math.exponential;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class PowerPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public PowerPipelineNodeConfiguration() {
        super("Power", "Power", "Math/Exponential");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("a", "A", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("b", "B", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        new SameTypeOutputTypeFunction<PipelineFieldType>("a", "b"),
                        PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
    }
}
