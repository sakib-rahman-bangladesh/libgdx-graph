package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.IdentityValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.RangeFloatValue;
import com.gempukku.libgdx.graph.shader.particles.generator.value.StaticFloatValue;

public class PointParticleGenerator implements ParticleGenerator {
    private Vector3 location = new Vector3();
    private FloatValue seed;
    private FloatValue lifeLength;

    public PointParticleGenerator(float lifeLength) {
        this(new StaticFloatValue(lifeLength));
    }

    public PointParticleGenerator(float minLifeLength, float maxLifeLength) {
        this(new RangeFloatValue(minLifeLength, maxLifeLength));
    }

    public PointParticleGenerator(FloatValue lifeLength) {
        this(IdentityValue.Instance, lifeLength);
    }

    public PointParticleGenerator(FloatValue seed, FloatValue lifeLength) {
        this.seed = seed;
        this.lifeLength = lifeLength;
    }

    public Vector3 getLocation() {
        return location;
    }

    @Override
    public void generateParticle(ParticleInfo particle) {
        particle.location.set(location);
        particle.seed = seed.getValue(MathUtils.random());
        particle.lifeLength = lifeLength.getValue(MathUtils.random());
    }
}
