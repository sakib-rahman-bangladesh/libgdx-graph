package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SpritePositionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpritePositionShaderNodeConfiguration() {
        super("SpritePosition", "Sprite Position", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector2));
    }
}
