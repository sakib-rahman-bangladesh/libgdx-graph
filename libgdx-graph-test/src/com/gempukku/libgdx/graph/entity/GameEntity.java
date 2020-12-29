package com.gempukku.libgdx.graph.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.system.PhysicsSystem;

public class GameEntity<T extends Sprite> {
    private T sprite;
    private Body body;
    private boolean dynamic;

    public GameEntity(T sprite) {
        this.sprite = sprite;
    }

    public T getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void createDynamicBody(PhysicsSystem physicsSystem, Vector2 colliderAnchor, Vector2 colliderScale) {
        body = physicsSystem.createDynamicBody(sprite, colliderAnchor, colliderScale);
        dynamic = true;
    }

    public void createStaticBody(PhysicsSystem physicsSystem, Vector2 colliderAnchor, Vector2 colliderScale) {
        body = physicsSystem.createStaticBody(sprite, colliderAnchor, colliderScale);
        dynamic = false;
    }
}
