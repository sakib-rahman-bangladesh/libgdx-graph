package com.gempukku.libgdx.graph.sprite.def;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class SpriteDef {
    // Sprite
    private float layer;
    private String[] tags;
    private String spriteType;

    // Common attributes
    private Vector2 position;
    private Vector2 size;
    private Vector2 anchor;

    // State based attributes
    private SpriteFaceDirection faceDirection;
    private String state;
    private ObjectMap<String, SpriteStateDataDef> stateData;

    // Tiled attributes
    private String tileTexture;
    private float u;
    private float v;
    private float u2;
    private float v2;
    private Vector2 tileRepeat;

    // Physics def
    private PhysicsDef physicsDef;

    public float getLayer() {
        return layer;
    }

    public String[] getTags() {
        return tags;
    }

    public String getSpriteType() {
        return spriteType;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getAnchor() {
        return anchor;
    }

    public SpriteFaceDirection getFaceDirection() {
        return faceDirection;
    }

    public String getState() {
        return state;
    }

    public ObjectMap<String, SpriteStateDataDef> getStateData() {
        return stateData;
    }

    public PhysicsDef getPhysicsDef() {
        return physicsDef;
    }

    public String getTileTexture() {
        return tileTexture;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public float getU2() {
        return u2;
    }

    public float getV2() {
        return v2;
    }

    public Vector2 getTileRepeat() {
        return tileRepeat;
    }
}
