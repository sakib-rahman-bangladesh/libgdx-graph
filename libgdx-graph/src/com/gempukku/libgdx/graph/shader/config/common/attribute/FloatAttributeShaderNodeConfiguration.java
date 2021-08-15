package com.gempukku.libgdx.graph.shader.config.common.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FloatAttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public FloatAttributeShaderNodeConfiguration() {
        super("FloatAttribute", "Float attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
