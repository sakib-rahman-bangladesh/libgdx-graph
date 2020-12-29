package com.gempukku.libgdx.graph.sprite.def;

import com.badlogic.gdx.math.Vector2;

public class PhysicsDef {
    private String type;
    private Vector2 colliderAnchor;
    private Vector2 colliderScale;
    private String category;
    private String[] mask;

    private SensorDef[] sensors;

    public String getType() {
        return type;
    }

    public Vector2 getColliderAnchor() {
        return colliderAnchor;
    }

    public Vector2 getColliderScale() {
        return colliderScale;
    }

    public String getCategory() {
        return category;
    }

    public String[] getMask() {
        return mask;
    }

    public SensorDef[] getSensors() {
        return sensors;
    }
}
