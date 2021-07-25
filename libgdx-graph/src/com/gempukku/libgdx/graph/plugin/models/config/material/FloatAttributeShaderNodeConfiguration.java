package com.gempukku.libgdx.graph.plugin.models.config.material;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FloatAttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public FloatAttributeShaderNodeConfiguration(String type, String name) {
        super(type, name, "Material");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", ShaderFieldType.Float));
    }
}
