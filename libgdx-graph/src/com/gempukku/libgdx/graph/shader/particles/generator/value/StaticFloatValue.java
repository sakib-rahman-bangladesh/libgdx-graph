package com.gempukku.libgdx.graph.shader.particles.generator.value;

public class StaticFloatValue implements FloatValue {
    private final float value;

    public StaticFloatValue(float value) {
        this.value = value;
    }

    @Override
    public float getValue(float seed) {
        return value;
    }
}
