package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpriteUVShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpriteUVShaderNodeConfiguration() {
        super("SpriteUV", "Sprite UV", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("uv", "UV", ShaderFieldType.Vector2));
    }
}
