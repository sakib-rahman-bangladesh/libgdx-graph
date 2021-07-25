package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;

public class ValueFloatPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueFloatPipelineNodeConfiguration() {
        super("ValueFloat", "Float", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Float));
    }
}
