package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;

public class TimePipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public TimePipelineNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("time", "Time", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("sinTime", "sin(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("cosTime", "cos(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("deltaTime", "deltaTime", Float));
    }
}
