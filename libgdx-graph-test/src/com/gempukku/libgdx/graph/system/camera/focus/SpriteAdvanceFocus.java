package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class SpriteAdvanceFocus implements WeightedCameraFocus {
    private Entity entity;
    private float advanceDistance;
    private float weight;

    public SpriteAdvanceFocus(Entity entity, float advanceDistance) {
        this(entity, advanceDistance, 1f);
    }

    public SpriteAdvanceFocus(Entity entity, float advanceDistance, float weight) {
        this.entity = entity;
        this.advanceDistance = advanceDistance;
        this.weight = weight;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        FacingComponent facing = entity.getComponent(FacingComponent.class);

        Vector2 result = focus.set(position.getPosition());
        SpriteFaceDirection faceDirection = facing.getFaceDirection();
        result.add(faceDirection.getX() * advanceDistance, faceDirection.getY() * advanceDistance);
        return result;
    }
}
