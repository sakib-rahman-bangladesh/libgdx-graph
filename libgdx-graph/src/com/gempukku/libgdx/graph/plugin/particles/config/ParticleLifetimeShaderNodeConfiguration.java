package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticleLifetimeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ParticleLifetimeShaderNodeConfiguration() {
        super("ParticleLifetime", "Particle lifetime", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("time", "Time", ShaderFieldType.Float));
    }
}
