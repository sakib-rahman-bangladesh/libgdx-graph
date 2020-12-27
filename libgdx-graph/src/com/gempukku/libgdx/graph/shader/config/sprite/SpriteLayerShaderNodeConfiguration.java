package com.gempukku.libgdx.graph.shader.config.sprite;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpriteLayerShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpriteLayerShaderNodeConfiguration() {
        super("SpriteLayer", "Sprite Layer", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("layer", "Layer", ShaderFieldType.Float));
    }
}
