package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Lighting3DEnvironment {
    private Vector3 sceneCenter = new Vector3();
    private float sceneDiameter;

    private LightColor ambientColor = new LightColor(Color.WHITE);
    private Array<Directional3DLight> directionalLights = new Array<>();
    private Array<Point3DLight> pointLights = new Array<>();
    private Array<Spot3DLight> spotLights = new Array<>();

    public Lighting3DEnvironment() {
        updateScene(new Vector3(0, 0, 0), 1f);
    }

    public Lighting3DEnvironment(Vector3 sceneCenter, float sceneDiameter) {
        updateScene(sceneCenter, sceneDiameter);
    }

    public void updateScene(Vector3 sceneCenter, float sceneDiameter) {
        this.sceneCenter.set(sceneCenter);
        this.sceneDiameter = sceneDiameter;
    }

    public Vector3 getSceneCenter() {
        return sceneCenter;
    }

    public float getSceneDiameter() {
        return sceneDiameter;
    }

    public LightColor getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Color ambientColor) {
        this.ambientColor.set(ambientColor);
    }

    public void addDirectionalLight(Directional3DLight directionalLight) {
        directionalLights.add(directionalLight);
    }

    public void removeDirectionalLight(Directional3DLight directionalLight) {
        directionalLights.removeValue(directionalLight, true);
    }

    public void addPointLight(Point3DLight pointLight) {
        pointLights.add(pointLight);
    }

    public void removePointLight(Point3DLight pointLight) {
        pointLights.removeValue(pointLight, true);
    }

    public void addSpotLight(Spot3DLight spotLight) {
        spotLights.add(spotLight);
    }

    public void removeSpotLight(Spot3DLight spotLight) {
        spotLights.removeValue(spotLight, true);
    }

    public Array<Directional3DLight> getDirectionalLights() {
        return directionalLights;
    }

    public Array<Point3DLight> getPointLights() {
        return pointLights;
    }

    public Array<Spot3DLight> getSpotLights() {
        return spotLights;
    }
}
