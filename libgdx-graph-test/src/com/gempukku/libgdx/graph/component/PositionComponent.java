package com.gempukku.libgdx.graph.component;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends DirtyComponent {
    private Vector2 position = new Vector2();

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        setDirty();
    }
}
