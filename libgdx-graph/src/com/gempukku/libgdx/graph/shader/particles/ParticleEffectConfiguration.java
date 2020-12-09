package com.gempukku.libgdx.graph.shader.particles;

public class ParticleEffectConfiguration {
    private int maxNumberOfParticles;
    private int initialParticles;
    private float particleDelay;

    public ParticleEffectConfiguration(int maxNumberOfParticles, int initialParticles, float particleDelay) {
        this.maxNumberOfParticles = maxNumberOfParticles;
        this.initialParticles = initialParticles;
        this.particleDelay = particleDelay;
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
