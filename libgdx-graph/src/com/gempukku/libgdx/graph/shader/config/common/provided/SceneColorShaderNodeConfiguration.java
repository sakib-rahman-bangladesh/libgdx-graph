package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SceneColorShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SceneColorShaderNodeConfiguration() {
        super("SceneColor", "Scene color", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
