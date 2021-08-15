package com.gempukku.libgdx.graph.shader.config.common.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ColorAttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public ColorAttributeShaderNodeConfiguration() {
        super("ColorAttribute", "Color attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Vector4));
    }
}
