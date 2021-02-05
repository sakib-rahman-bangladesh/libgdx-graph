package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;

public class ValueColorPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Color));
    }
}
