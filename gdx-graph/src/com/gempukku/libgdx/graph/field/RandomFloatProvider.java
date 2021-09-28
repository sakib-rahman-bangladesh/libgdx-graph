package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.math.MathUtils;

public class RandomFloatProvider implements FloatProvider {
    private float min;
    private float max;

    public RandomFloatProvider(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public float get() {
        return MathUtils.random(min, max);
    }
}
