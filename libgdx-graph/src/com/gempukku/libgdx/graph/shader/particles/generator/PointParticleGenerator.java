package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;

public class PointParticleGenerator<T> extends AbstractParticleGenerator<T> {
    private Vector3 location = new Vector3();

    public PointParticleGenerator(float lifeLength) {
        super(lifeLength);
    }

    public PointParticleGenerator(float minLifeLength, float maxLifeLength) {
        super(minLifeLength, maxLifeLength);
    }

    public PointParticleGenerator(FloatValue lifeLength) {
        super(lifeLength);
    }

    public PointParticleGenerator(FloatValue seed, FloatValue lifeLength) {
        super(seed, lifeLength);
    }

    public PointParticleGenerator(FloatValue seed, FloatValue lifeLength, ParticleDataGenerator<T> particleDataGenerator) {
        super(seed, lifeLength, particleDataGenerator);
    }

    public Vector3 getLocation() {
        return location;
    }

    @Override
    protected void generateLocation(ParticleGenerateInfo<T> particle) {
        particle.location.set(location);
    }
}
