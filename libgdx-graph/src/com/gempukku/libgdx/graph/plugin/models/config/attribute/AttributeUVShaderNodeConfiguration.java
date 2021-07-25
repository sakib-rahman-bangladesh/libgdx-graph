package com.gempukku.libgdx.graph.plugin.models.config.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AttributeUVShaderNodeConfiguration extends NodeConfigurationImpl {
    public AttributeUVShaderNodeConfiguration() {
        super("AttributeUV", "UV attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("uv", "UV", ShaderFieldType.Vector2));
    }
}
