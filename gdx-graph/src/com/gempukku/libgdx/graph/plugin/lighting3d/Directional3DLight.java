package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBuffer;

public class Directional3DLight {
    private Vector3 direction = new Vector3(0, 0, 0);
    private LightColor color = new LightColor(1, 1, 1);
    private float intensity = 1f;
    private boolean shadowsEnabled = false;

    private OrthographicCamera shadowCamera = new OrthographicCamera();
    private TextureFrameBuffer shadowTextureFrameBuffer;

    public Directional3DLight() {

    }

    public Directional3DLight(DirectionalLight directionalLight) {
        setDirection(directionalLight.direction);
        setColor(directionalLight.color);
        setIntensity(1f);
    }

    public boolean isShadowsEnabled() {
        return shadowsEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
    }

    public void updateCamera(Vector3 sceneCenter, float sceneDiameter) {
        shadowCamera.viewportWidth = sceneDiameter;
        shadowCamera.viewportHeight = sceneDiameter;
        shadowCamera.near = 0;
        shadowCamera.far = sceneDiameter;
        shadowCamera.position.set(sceneCenter.x - sceneDiameter / 2f, sceneCenter.y, sceneCenter.z);
        shadowCamera.update();
    }

    public OrthographicCamera getShadowCamera() {
        return shadowCamera;
    }

    public TextureFrameBuffer getShadowTextureFrameBuffer() {
        return shadowTextureFrameBuffer;
    }

    public void setShadowTextureFrameBuffer(TextureFrameBuffer shadowTextureFrameBuffer) {
        this.shadowTextureFrameBuffer = shadowTextureFrameBuffer;
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
