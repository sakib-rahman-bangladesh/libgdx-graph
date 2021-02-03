package com.gempukku.libgdx.graph.plugin.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Predicate;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.FloatValue;

public class LineParticleGenerator<T> extends AbstractParticleGenerator<T> {
    private Vector3 point1 = new Vector3();
    private Vector3 point2 = new Vector3();

    public LineParticleGenerator(float lifeLength) {
        super(lifeLength);
    }

    public LineParticleGenerator(float minLifeLength, float maxLifeLength) {
        super(minLifeLength, maxLifeLength);
    }

    public LineParticleGenerator(FloatValue lifeLength) {
        super(lifeLength);
    }

    public LineParticleGenerator(FloatValue lifeLength, Predicate<Vector3> locationPredicate) {
        super(lifeLength, locationPredicate);
    }

    public LineParticleGenerator(FloatValue lifeLength, Predicate<Vector3> locationPredicate, ParticleDataGenerator<T> particleDataGenerator) {
        super(lifeLength, locationPredicate, particleDataGenerator);
    }

    public Vector3 getPoint1() {
        return point1;
    }

    public Vector3 getPoint2() {
        return point2;
    }

    @Override
    protected void generateLocation(Vector3 location) {
        float value = MathUtils.random();
        location.set(
                point1.x + (point2.x - point1.x) * value,
                point1.y + (point2.y - point1.y) * value,
                point1.z + (point2.z - point1.z) * value);
    }
}
