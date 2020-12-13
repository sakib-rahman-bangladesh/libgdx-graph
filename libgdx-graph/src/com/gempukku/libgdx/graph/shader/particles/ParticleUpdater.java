package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.math.Vector3;

public interface ParticleUpdater<T> {
    void updateParticle(ParticleUpdateInfo<T> particleUpdateInfo);

    class ParticleUpdateInfo<T> {
        public final Vector3 location = new Vector3();
        public float seed;
        public T particleData;
    }
}
