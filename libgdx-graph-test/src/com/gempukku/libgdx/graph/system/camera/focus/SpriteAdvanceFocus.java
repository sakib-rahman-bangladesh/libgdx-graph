package com.gempukku.libgdx.graph.system.camera.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.sprite.FacedSprite;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class SpriteAdvanceFocus implements WeightedCameraFocus {
    private FacedSprite facedSprite;
    private float advanceDistance;
    private float weight;

    public SpriteAdvanceFocus(FacedSprite facedSprite, float advanceDistance) {
        this(facedSprite, advanceDistance, 1f);
    }

    public SpriteAdvanceFocus(FacedSprite facedSprite, float advanceDistance, float weight) {
        this.facedSprite = facedSprite;
        this.advanceDistance = advanceDistance;
        this.weight = weight;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        Vector2 result = facedSprite.getPosition(focus);
        SpriteFaceDirection faceDirection = facedSprite.getFaceDirection();
        result.add(faceDirection.getX() * advanceDistance, faceDirection.getY() * advanceDistance);
        return result;
    }
}
