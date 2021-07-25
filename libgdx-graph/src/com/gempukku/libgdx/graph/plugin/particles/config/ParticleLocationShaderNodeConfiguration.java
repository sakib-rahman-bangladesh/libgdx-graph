package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticleLocationShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ParticleLocationShaderNodeConfiguration() {
        super("ParticleLocation", "Particle location", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector3));
    }
}
