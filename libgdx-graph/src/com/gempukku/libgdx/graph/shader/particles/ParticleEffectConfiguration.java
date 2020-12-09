package com.gempukku.libgdx.graph.shader.particles;

public class ParticleEffectConfiguration {
    private int maxNumberOfParticles;

    public ParticleEffectConfiguration(int maxNumberOfParticles) {
        this.maxNumberOfParticles = maxNumberOfParticles;
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }
}
