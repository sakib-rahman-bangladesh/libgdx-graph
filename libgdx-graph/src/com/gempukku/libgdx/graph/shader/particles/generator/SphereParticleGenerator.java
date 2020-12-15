package com.gempukku.libgdx.graph.shader.particles.generator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.particles.generator.value.FloatValue;

public class SphereParticleGenerator<T> extends AbstractParticleGenerator<T> {
    private Vector3 center = new Vector3();
    private float radius = 1f;

    public SphereParticleGenerator(float lifeLength) {
        super(lifeLength);
    }

    public SphereParticleGenerator(float minLifeLength, float maxLifeLength) {
        super(minLifeLength, maxLifeLength);
    }

    public SphereParticleGenerator(FloatValue lifeLength) {
        super(lifeLength);
    }

    public SphereParticleGenerator(FloatValue seed, FloatValue lifeLength) {
        super(seed, lifeLength);
    }

    public SphereParticleGenerator(FloatValue seed, FloatValue lifeLength, ParticleDataGenerator<T> particleDataGenerator) {
        super(seed, lifeLength, particleDataGenerator);
    }

    public Vector3 getCenter() {
        return center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected void generateLocation(ParticleGenerateInfo<T> particle) {
        // Uniformly distribute the points on sphere using Marsaglia (1972) method from
        // https://mathworld.wolfram.com/SpherePointPicking.html

        while (true) {
            float x1 = MathUtils.random(-1f, 1f);
            float x2 = MathUtils.random(-1f, 1f);
            float x1Square = x1 * x1;
            float x2Square = x2 * x2;
            if (x1Square + x2Square < 1f) {
                float a = (float) Math.sqrt(1 - x1Square - x2Square);
                float x = 2 * x1 * a;
                float y = 2 * x2 * a;
                float z = 1 - 2 * (x1Square + x2Square);

                float r = (float) Math.pow(MathUtils.random(0, radius), 1d / 3d);
                particle.location.set(r * x, r * y, r * z);
                break;
            }
        }
    }
}
