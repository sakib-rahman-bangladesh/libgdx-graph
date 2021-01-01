package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class AnchorComponent implements Component {
    private Vector2 anchor;

    public Vector2 getAnchor() {
        return anchor;
    }

    public void setAnchor(Vector2 anchor) {
        this.anchor = anchor;
    }
}
