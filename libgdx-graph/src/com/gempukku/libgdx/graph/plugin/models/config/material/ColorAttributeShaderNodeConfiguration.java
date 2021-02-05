package com.gempukku.libgdx.graph.plugin.models.config.material;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ColorAttributeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ColorAttributeShaderNodeConfiguration(String type, String name) {
        super(type, name, "Material");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Vector4));
    }
}
