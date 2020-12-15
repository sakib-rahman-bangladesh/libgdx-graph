package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Predicate;
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

    public PointParticleGenerator(FloatValue lifeLength, Predicate<Vector3> locationPredicate) {
        super(lifeLength, locationPredicate);
    }

    public PointParticleGenerator(FloatValue lifeLength, Predicate<Vector3> locationPredicate, ParticleDataGenerator<T> particleDataGenerator) {
        super(lifeLength, locationPredicate, particleDataGenerator);
    }

    public Vector3 getLocation() {
        return location;
    }

    @Override
    protected void generateLocation(Vector3 location) {
        location.set(this.location);
    }
}
