package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AmbientLightShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AmbientLightShaderNodeConfiguration() {
        super("AmbientLight", "Ambient light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("ambient", "Color", ShaderFieldType.Vector3));
    }
}
