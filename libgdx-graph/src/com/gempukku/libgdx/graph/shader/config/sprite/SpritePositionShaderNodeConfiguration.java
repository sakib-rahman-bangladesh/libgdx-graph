package com.gempukku.libgdx.graph.shader.config.sprite;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpritePositionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpritePositionShaderNodeConfiguration() {
        super("SpritePosition", "Sprite Position", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector2));
    }
}
