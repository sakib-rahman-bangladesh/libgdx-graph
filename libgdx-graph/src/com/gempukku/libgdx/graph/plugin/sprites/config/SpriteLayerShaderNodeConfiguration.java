package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpriteLayerShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpriteLayerShaderNodeConfiguration() {
        super("SpriteLayer", "Sprite Layer", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("layer", "Layer", ShaderFieldType.Float));
    }
}
