package com.gempukku.libgdx.graph.plugin.models.config.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AttributeUVShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AttributeUVShaderNodeConfiguration() {
        super("AttributeUV", "UV attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("uv", "UV", ShaderFieldType.Vector2));
    }
}
