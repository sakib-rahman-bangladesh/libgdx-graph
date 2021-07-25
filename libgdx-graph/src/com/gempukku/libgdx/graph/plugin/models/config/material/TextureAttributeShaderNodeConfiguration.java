package com.gempukku.libgdx.graph.plugin.models.config.material;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class TextureAttributeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public TextureAttributeShaderNodeConfiguration(String type, String name) {
        super(type, name, "Material");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
