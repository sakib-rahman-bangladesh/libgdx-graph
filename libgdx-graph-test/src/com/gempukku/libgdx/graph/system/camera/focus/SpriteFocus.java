package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.component.PositionComponent;

public class SpriteFocus implements WeightedCameraFocus {
    public Entity entity;
    private float weight;
    private float x;
    private float y;

    public SpriteFocus(Entity entity) {
        this(entity, 1f);
    }

    public SpriteFocus(Entity entity, float weight) {
        this(entity, weight, 0, 0);
    }

    public SpriteFocus(Entity entity, float weight, float x, float y) {
        this.entity = entity;
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        return position.getPosition(focus).add(x, y);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
