package com.gempukku.libgdx.graph.system.camera;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.sprite.Sprite;

public class SpriteFocus implements WeightedCameraFocus {
    public Sprite sprite;
    private float weight;

    public SpriteFocus(Sprite sprite) {
        this(sprite, 1f);
    }

    public SpriteFocus(Sprite sprite, float weight) {
        this.sprite = sprite;
        this.weight = weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        return sprite.getPosition(focus);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
