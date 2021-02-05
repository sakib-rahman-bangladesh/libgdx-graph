package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpriteSizeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpriteSizeShaderNodeConfiguration() {
        super("SpriteSize", "Sprite Size", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("size", "Size", ShaderFieldType.Vector2));
    }
}
