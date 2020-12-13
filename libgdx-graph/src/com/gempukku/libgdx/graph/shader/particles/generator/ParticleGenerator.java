package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.Vector3;

public interface ParticleGenerator<T> {
    void generateParticle(ParticleGenerateInfo<T> particle);

    class ParticleGenerateInfo<T> {
        public final Vector3 location = new Vector3();
        public float seed;
        public float lifeLength;
        public T particleData;
    }
}
