package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Boolean;

public class ValueBooleanPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Boolean));
    }
}
