package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Vector3;

public class ValueVector3PipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueVector3PipelineNodeConfiguration() {
        super("ValueVector3", "Vector3", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Vector3));
    }
}
