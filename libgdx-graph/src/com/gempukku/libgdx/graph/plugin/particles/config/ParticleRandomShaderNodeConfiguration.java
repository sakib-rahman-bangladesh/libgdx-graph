package com.gempukku.libgdx.graph.plugin.particles.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ParticleRandomShaderNodeConfiguration extends NodeConfigurationImpl {
    public ParticleRandomShaderNodeConfiguration() {
        super("ParticleRandom", "Particle random", "Particle");
        addNodeOutput(
                new GraphNodeOutputImpl("value", "Value", ShaderFieldType.Float));
    }
}
