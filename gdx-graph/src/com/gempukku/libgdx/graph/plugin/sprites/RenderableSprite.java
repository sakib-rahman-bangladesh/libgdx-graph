package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public interface RenderableSprite {
    Vector3 getPosition();

    PropertyContainer getPropertyContainer(String tag);
}
