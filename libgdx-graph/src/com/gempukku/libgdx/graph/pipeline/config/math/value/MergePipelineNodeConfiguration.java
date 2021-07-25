package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class MergePipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("x", "X", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("y", "Y", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("z", "Z", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("w", "W", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("v2", "Vector2", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("v3", "Vector3", Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("color", "Color", Color));
    }
}
