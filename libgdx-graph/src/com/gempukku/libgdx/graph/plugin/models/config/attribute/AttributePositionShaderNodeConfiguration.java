package com.gempukku.libgdx.graph.plugin.models.config.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AttributePositionShaderNodeConfiguration extends NodeConfigurationImpl {
    public AttributePositionShaderNodeConfiguration() {
        super("AttributePosition", "Position attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("position", "Position", ShaderFieldType.Vector3));
    }
}
