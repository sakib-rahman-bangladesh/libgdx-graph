package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.math.Vector3;

public interface ParticleGenerator {
    void generateParticle(ParticleInfo particle);

    class ParticleInfo {
        public Vector3 location;
        public float randomValue;
        public float lifeLength;
    }
}
