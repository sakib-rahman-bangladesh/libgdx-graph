package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SceneColorShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SceneColorShaderNodeConfiguration() {
        super("SceneColor", "Scene color", "Provided");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("screenPosition", "Screen position", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Color));
    }
}
