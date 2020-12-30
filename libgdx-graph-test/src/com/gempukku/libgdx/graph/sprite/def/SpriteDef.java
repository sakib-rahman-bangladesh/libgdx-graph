package com.gempukku.libgdx.graph.sprite.def;

import com.badlogic.gdx.math.Vector2;

public class SpriteDef {
    // Sprite
    private float layer;
    private String[] tags;
    private String spriteType;

    // Common attributes
    private Vector2 position;
    private Vector2 size;
    private Vector2 anchor;

    private StateBasedSpriteDef stateBasedSprite;
    private TiledSpriteDef tiledSprite;
    private SimpleSpriteDef simpleSprite;

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

    public StateBasedSpriteDef getStateBasedSprite() {
        return stateBasedSprite;
    }

    public TiledSpriteDef getTiledSprite() {
        return tiledSprite;
    }

    public SimpleSpriteDef getSimpleSprite() {
        return simpleSprite;
    }

    public PhysicsDef getPhysicsDef() {
        return physicsDef;
    }
}
