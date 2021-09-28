package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.math.Vector3;

public interface GraphSprite {
    Vector3 getPosition(Vector3 position);

    boolean hasTag(String tag);

    Iterable<String> getAllTags();
}
