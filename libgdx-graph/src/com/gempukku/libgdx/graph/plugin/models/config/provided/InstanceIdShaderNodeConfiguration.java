package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class InstanceIdShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public InstanceIdShaderNodeConfiguration() {
        super("InstanceID", "Instance ID", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("id", "Id", ShaderFieldType.Float));
    }
}
