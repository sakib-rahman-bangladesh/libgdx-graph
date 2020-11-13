package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SceneColorShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SceneColorShaderNodeConfiguration() {
        super("SceneColor", "Scene color", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
