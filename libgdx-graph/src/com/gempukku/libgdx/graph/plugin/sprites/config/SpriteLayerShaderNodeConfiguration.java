package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SpriteLayerShaderNodeConfiguration extends NodeConfigurationImpl {
    public SpriteLayerShaderNodeConfiguration() {
        super("SpriteLayer", "Sprite Layer", "Sprite");
        addNodeOutput(
                new GraphNodeOutputImpl("layer", "Layer", ShaderFieldType.Float));
    }
}
