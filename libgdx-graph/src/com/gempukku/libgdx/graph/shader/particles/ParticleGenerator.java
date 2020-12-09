package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.math.Vector3;

public interface ParticleGenerator {
    void generateParticle(ParticleInfo particle);

    class ParticleInfo {
        public final Vector3 location = new Vector3();
        public float seed;
        public float lifeLength;
    }
}
