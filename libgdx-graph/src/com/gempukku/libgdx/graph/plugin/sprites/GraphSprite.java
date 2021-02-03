package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.math.Vector2;

public interface GraphSprite {
    Vector2 getPosition(Vector2 position);

    boolean hasTag(String tag);

    Iterable<String> getAllTags();
}
