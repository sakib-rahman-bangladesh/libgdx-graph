package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.IdentityValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.StaticFloatValue;

public abstract class AbstractParticleGenerator<T> implements ParticleGenerator<T> {
    private FloatValue seed;
    private FloatValue lifeLength;
    private ParticleDataGenerator<T> particleDataGenerator;

    public AbstractParticleGenerator(float lifeLength) {
        this(new StaticFloatValue(lifeLength));
    }

    public AbstractParticleGenerator(float minLifeLength, float maxLifeLength) {
        this(new RangeFloatValue(minLifeLength, maxLifeLength));
    }

    public AbstractParticleGenerator(FloatValue lifeLength) {
        this(IdentityValue.Instance, lifeLength);
    }

    public AbstractParticleGenerator(FloatValue seed, FloatValue lifeLength) {
        this(seed, lifeLength, null);
    }

    public AbstractParticleGenerator(FloatValue seed, FloatValue lifeLength, ParticleDataGenerator<T> particleDataGenerator) {
        this.seed = seed;
        this.lifeLength = lifeLength;
        this.particleDataGenerator = particleDataGenerator;
    }

    @Override
    public void generateParticle(ParticleGenerateInfo<T> particle) {
        generateLocation(particle);
        particle.seed = seed.getValue(MathUtils.random());
        particle.lifeLength = lifeLength.getValue(MathUtils.random());
        if (particleDataGenerator != null)
            particle.particleData = particleDataGenerator.generateData();
    }

    protected abstract void generateLocation(ParticleGenerateInfo<T> particle);
}
