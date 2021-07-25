package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class SplitPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, Color, Vector3, Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("x", "X", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("y", "Y", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("z", "Z", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("w", "W", Float));
    }
}
