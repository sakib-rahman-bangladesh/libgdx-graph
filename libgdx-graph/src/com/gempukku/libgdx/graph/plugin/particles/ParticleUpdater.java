package com.gempukku.libgdx.graph.plugin.particles;

public interface ParticleUpdater<T> {
    void updateParticle(ParticleUpdateInfo<T> particleUpdateInfo);

    class ParticleUpdateInfo<T> {
        public T particleData;
    }
}
