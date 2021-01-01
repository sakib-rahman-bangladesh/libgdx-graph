package com.gempukku.libgdx.graph.sprite.def;

import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class SpriteDef {
    // Sprite
    private float layer;
    private String[] tags;
    private String spriteType;
    private SpriteFaceDirection facing;

    private StateBasedSpriteDef stateBasedSprite;
    private TiledSpriteDef tiledSprite;
    private SimpleSpriteDef simpleSprite;

    public SpriteFaceDirection getFacing() {
        return facing;
    }

    public float getLayer() {
        return layer;
    }

    public String[] getTags() {
        return tags;
    }

    public String getSpriteType() {
        return spriteType;
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
}
