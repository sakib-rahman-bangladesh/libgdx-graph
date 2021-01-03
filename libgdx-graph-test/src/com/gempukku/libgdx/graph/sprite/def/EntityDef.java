package com.gempukku.libgdx.graph.sprite.def;

import com.badlogic.gdx.math.Vector2;

public class EntityDef {
    // Common attributes
    private Vector2 position;
    private Vector2 size;
    private Vector2 anchor;

    private SpriteDef spriteDef;
    private OutlineDef outlineDef;
    private PhysicsDef physicsDef;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getAnchor() {
        return anchor;
    }

    public SpriteDef getSpriteDef() {
        return spriteDef;
    }

    public OutlineDef getOutlineDef() {
        return outlineDef;
    }

    public PhysicsDef getPhysicsDef() {
        return physicsDef;
    }
}
