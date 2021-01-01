package com.gempukku.libgdx.graph.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.system.PhysicsSystem;

public class GameEntity<T extends Sprite> {
    private T sprite;
    private Body body;
    private boolean dynamic;
    private Array<SensorData> sensorData = new Array<>();

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

    public void createDynamicBody(PhysicsSystem physicsSystem, GameEntity<?> gameEntity, Vector2 colliderAnchor, Vector2 colliderScale,
                                  String[] category, String[] mask) {
        body = physicsSystem.createDynamicBody(gameEntity, sprite, colliderAnchor, colliderScale, category, mask);
        dynamic = true;
    }

    public void createStaticBody(PhysicsSystem physicsSystem, GameEntity<?> gameEntity, Vector2 colliderAnchor, Vector2 colliderScale,
                                 String[] category, String[] mask) {
        body = physicsSystem.createStaticBody(gameEntity, sprite, colliderAnchor, colliderScale, category, mask);
        dynamic = false;
    }

    public void createSensor(PhysicsSystem physicsSystem, String type, Vector2 sensorAnchor, Vector2 sensorScale, String[] mask) {
        sensorData.add(physicsSystem.createSensor(sprite, body, type, sensorAnchor, sensorScale, mask));
    }

    public SensorData getSensorDataOfType(String type) {
        for (SensorData sensorDatum : sensorData) {
            if (sensorDatum.getType().equals(type))
                return sensorDatum;
        }
        return null;
    }
}
