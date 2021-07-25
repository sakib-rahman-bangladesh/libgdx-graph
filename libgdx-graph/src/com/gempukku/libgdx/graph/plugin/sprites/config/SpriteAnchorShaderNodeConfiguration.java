package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SpriteAnchorShaderNodeConfiguration extends NodeConfigurationImpl {
    public SpriteAnchorShaderNodeConfiguration() {
        super("SpriteAnchor", "Sprite Anchor", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl("anchor", "Anchor", ShaderFieldType.Vector2));
    }
}
