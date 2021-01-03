package com.gempukku.libgdx.graph.component;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends DirtyComponent {
    private float x;
    private float y;

    public Vector2 getPosition(Vector2 position) {
        return position.set(x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        setDirty();
    }
}
