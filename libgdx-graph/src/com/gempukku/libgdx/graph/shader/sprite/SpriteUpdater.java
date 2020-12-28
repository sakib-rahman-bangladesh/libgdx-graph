package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.math.Vector2;

public interface SpriteUpdater {
    float processUpdate(float layer, Vector2 position, Vector2 size, Vector2 anchor);
}
