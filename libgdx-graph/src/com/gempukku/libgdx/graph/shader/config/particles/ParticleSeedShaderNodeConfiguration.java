package com.gempukku.libgdx.graph.shader.config.particles;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ParticleSeedShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ParticleSeedShaderNodeConfiguration() {
        super("ParticleSeed", "Particle seed", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("seed", "Seed", ShaderFieldType.Float));
    }
}
