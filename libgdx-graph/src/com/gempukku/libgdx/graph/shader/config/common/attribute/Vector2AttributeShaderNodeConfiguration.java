package com.gempukku.libgdx.graph.shader.config.common.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector2AttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public Vector2AttributeShaderNodeConfiguration() {
        super("Vector2Attribute", "Vector2 attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Vector2));
    }
}
