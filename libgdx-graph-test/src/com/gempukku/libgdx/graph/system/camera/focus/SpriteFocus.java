package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.component.PositionComponent;

public class SpriteFocus implements WeightedCameraFocus {
    public Entity entity;
    private float weight;

    public SpriteFocus(Entity entity) {
        this(entity, 1f);
    }

    public SpriteFocus(Entity entity, float weight) {
        this.entity = entity;
        this.weight = weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        return position.getPosition(focus);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
