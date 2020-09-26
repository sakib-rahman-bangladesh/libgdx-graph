package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.math.Matrix4;

public class Transforms {
    private Transforms() {
    }

    public static TransformUpdate translate(float x, float y, float z) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.translate(x, y, z);
            }
        };
    }

    public static TransformUpdate rotate(float axisX, float axisY, float axisZ, float degrees) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.rotate(axisX, axisY, axisZ, degrees);
            }
        };
    }

    public static TransformUpdate scale(float scaleX, float scaleY, float scaleZ) {
        return new TransformUpdate() {
            @Override
            public void updateTransform(Matrix4 transform) {
                transform.scale(scaleX, scaleY, scaleZ);
            }
        };
    }
}
