package com.gempukku.libgdx.graph.shader.node.noise;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GLSLAdapter {

    public static Vector3 fract(Vector3 v) {
        return new Vector3(fract(v.x), fract(v.y), fract(v.z));
    }

    public static Vector3 floor(Vector3 v) {
        return new Vector3(MathUtils.floor(v.x), MathUtils.floor(v.y), MathUtils.floor(v.z));
    }

    public static Vector2 floor(Vector2 v) {
        return new Vector2(MathUtils.floor(v.x), MathUtils.floor(v.y));
    }

    public static float fract(float v) {
        return v - MathUtils.floor(v);
    }

    public static Vector3 abs(Vector3 v) {
        return new Vector3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
    }

    public static float dot(Vector2 v1, Vector2 v2) {
        return v1.dot(v2);
    }

}
