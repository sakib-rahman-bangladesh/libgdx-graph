package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;

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

    public LineParticleGenerator(FloatValue seed, FloatValue lifeLength) {
        super(seed, lifeLength);
    }

    public LineParticleGenerator(FloatValue seed, FloatValue lifeLength, ParticleDataGenerator<T> particleDataGenerator) {
        super(seed, lifeLength, particleDataGenerator);
    }

    public Vector3 getPoint1() {
        return point1;
    }

    public Vector3 getPoint2() {
        return point2;
    }

    @Override
    protected void generateLocation(ParticleGenerateInfo<T> particle) {
        float value = MathUtils.random();
        particle.location.set(
                point1.x + (point2.x - point1.x) * value,
                point1.y + (point2.y - point1.y) * value,
                point1.z + (point2.z - point1.z) * value);
    }
}
