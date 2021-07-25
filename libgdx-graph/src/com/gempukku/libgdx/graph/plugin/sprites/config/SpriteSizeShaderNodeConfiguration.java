package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SpriteSizeShaderNodeConfiguration extends NodeConfigurationImpl {
    public SpriteSizeShaderNodeConfiguration() {
        super("SpriteSize", "Sprite Size", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl("size", "Size", ShaderFieldType.Vector2));
    }
}
