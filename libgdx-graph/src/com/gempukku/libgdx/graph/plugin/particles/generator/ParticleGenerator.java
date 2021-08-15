package com.gempukku.libgdx.graph.plugin.particles.generator;

import java.util.HashMap;
import java.util.Map;

public interface ParticleGenerator<T> {
    void generateParticle(ParticleGenerateInfo<T> particle);

    class ParticleGenerateInfo<T> {
        public float lifeLength;
        public T particleData;
        public Map<String, Object> particleAttributes = new HashMap<>();
    }
}
