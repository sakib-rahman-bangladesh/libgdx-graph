package com.gempukku.libgdx.graph.plugin.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.plugin.particles.generator.value.StaticFloatValue;

public class DefaultParticleGenerator<T> implements ParticleGenerator<T> {
    private FloatValue lifeLength;
    private ParticleDataGenerator<T> particleDataGenerator;
    private String positionAttribute = "Position";
    private PositionGenerator positionGenerator;
    private Vector3 tmpVector = new Vector3();

    public DefaultParticleGenerator(float lifeLength) {
        this(new StaticFloatValue(lifeLength));
    }

    public DefaultParticleGenerator(float minLifeLength, float maxLifeLength) {
        this(new RangeFloatValue(minLifeLength, maxLifeLength));
    }

    public DefaultParticleGenerator(FloatValue lifeLength) {
        this(lifeLength, null);
    }

    public DefaultParticleGenerator(FloatValue lifeLength,
                                    ParticleDataGenerator<T> particleDataGenerator) {
        this.lifeLength = lifeLength;
        this.particleDataGenerator = particleDataGenerator;
    }

    public void setPositionAttribute(String positionAttribute) {
        this.positionAttribute = positionAttribute;
    }

    public void setPositionGenerator(PositionGenerator positionGenerator) {
        this.positionGenerator = positionGenerator;
    }

    @Override
    public void generateParticle(ParticleGenerateInfo<T> particle) {
        particle.lifeLength = lifeLength.getValue(MathUtils.random());
        if (particleDataGenerator != null)
            particle.particleData = particleDataGenerator.generateData();
        if (positionGenerator != null)
            particle.particleAttributes.put(positionAttribute, positionGenerator.generateLocation(tmpVector));
        generateAttributes(particle);
    }

    protected void generateAttributes(ParticleGenerateInfo<T> particle) {

    }
}
