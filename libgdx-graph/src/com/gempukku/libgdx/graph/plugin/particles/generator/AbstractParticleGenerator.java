package com.gempukku.libgdx.graph.plugin.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.StaticFloatValue;

public abstract class AbstractParticleGenerator<T> implements ParticleGenerator<T> {
    private FloatValue lifeLength;
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

    public AbstractParticleGenerator(FloatValue lifeLength,
                                     ParticleDataGenerator<T> particleDataGenerator) {
        this.lifeLength = lifeLength;
        this.particleDataGenerator = particleDataGenerator;
    }

    @Override
    public void generateParticle(ParticleGenerateInfo<T> particle) {
        particle.lifeLength = lifeLength.getValue(MathUtils.random());
        if (particleDataGenerator != null)
            particle.particleData = particleDataGenerator.generateData();
        generateAttributes(particle);
    }

    protected abstract void generateAttributes(ParticleGenerateInfo<T> particle);
}
