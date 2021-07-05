package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

public class Directional3DLight {
    private Vector3 direction = new Vector3(0, 0, 0);
    private LightColor color = new LightColor(1, 1, 1);
    private float intensity = 1f;

    public Directional3DLight() {

    }

    public Directional3DLight(DirectionalLight directionalLight) {
        setDirection(directionalLight.direction);
        setColor(directionalLight.color);
        setIntensity(1f);
    }

    public Directional3DLight setDirection(Vector3 direction) {
        return setDirection(direction.x, direction.y, direction.z);
    }

    public Directional3DLight setDirection(float x, float y, float z) {
        direction.set(x, y, z).nor();
        return this;
    }

    public Directional3DLight setColor(Color color) {
        return setColor(color.r, color.g, color.b);
    }

    public Directional3DLight setColor(float red, float green, float blue) {
        color.set(red, green, blue);
        return this;
    }

    public Directional3DLight setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
    }

    public float getDirectionX() {
        return direction.x;
    }

    public float getDirectionY() {
        return direction.y;
    }

    public float getDirectionZ() {
        return direction.z;
    }

    public LightColor getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }
}
