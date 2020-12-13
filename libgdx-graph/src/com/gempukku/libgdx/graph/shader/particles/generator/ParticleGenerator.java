package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.Vector3;

public interface ParticleGenerator {
    void generateParticle(ParticleGenerateInfo particle);

    class ParticleGenerateInfo {
        public final Vector3 location = new Vector3();
        public float seed;
        public float lifeLength;
    }
}
