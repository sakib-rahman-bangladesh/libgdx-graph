package com.gempukku.libgdx.graph.plugin.models.config.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AttributeColorShaderNodeConfiguration extends NodeConfigurationImpl {
    public AttributeColorShaderNodeConfiguration() {
        super("AttributeColor", "Color attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("color", "Color", ShaderFieldType.Vector4));
    }
}
