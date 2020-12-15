package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Predicate;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.StaticFloatValue;

public abstract class AbstractParticleGenerator<T> implements ParticleGenerator<T> {
    private Vector3 temp = new Vector3();

    private FloatValue lifeLength;
    private Predicate<Vector3> locationPredicate;
    private ParticleDataGenerator<T> particleDataGenerator;

    public AbstractParticleGenerator(float lifeLength) {
        this(new StaticFloatValue(lifeLength));
    }

    public AbstractParticleGenerator(float minLifeLength, float maxLifeLength) {
        this(new RangeFloatValue(minLifeLength, maxLifeLength));
    }

    public AbstractParticleGenerator(FloatValue lifeLength) {
        this(lifeLength, null);
    }

    public AbstractParticleGenerator(FloatValue lifeLength, Predicate<Vector3> locationPredicate) {
        this(lifeLength, locationPredicate, null);
    }

    public AbstractParticleGenerator(FloatValue lifeLength,
                                     Predicate<Vector3> locationPredicate,
                                     ParticleDataGenerator<T> particleDataGenerator) {
        this.lifeLength = lifeLength;
        this.locationPredicate = locationPredicate;
        this.particleDataGenerator = particleDataGenerator;
    }

    @Override
    public void generateParticle(ParticleGenerateInfo<T> particle) {
        if (locationPredicate != null) {
            do {
                generateLocation(temp);
            } while (!locationPredicate.evaluate(temp));
            particle.location.set(temp);
        } else {
            generateLocation(particle.location);
        }
        particle.seed = MathUtils.random();
        particle.lifeLength = lifeLength.getValue(MathUtils.random());
        if (particleDataGenerator != null)
            particle.particleData = particleDataGenerator.generateData();
    }

    protected abstract void generateLocation(Vector3 location);
}
