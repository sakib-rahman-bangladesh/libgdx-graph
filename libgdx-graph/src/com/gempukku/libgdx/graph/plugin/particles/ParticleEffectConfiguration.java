package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.VertexAttributes;

public class ParticleEffectConfiguration {
    private VertexAttributes vertexAttributes;
    private int maxNumberOfParticles;
    private int initialParticles;
    private float particleDelay;

    public ParticleEffectConfiguration(VertexAttributes vertexAttributes, int maxNumberOfParticles, int initialParticles, float particleDelay) {
        this.vertexAttributes = vertexAttributes;
        this.maxNumberOfParticles = maxNumberOfParticles;
        this.initialParticles = initialParticles;
        this.particleDelay = particleDelay;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public int getInitialParticles() {
        return initialParticles;
    }

    public float getParticleDelay() {
        return particleDelay;
    }
}
