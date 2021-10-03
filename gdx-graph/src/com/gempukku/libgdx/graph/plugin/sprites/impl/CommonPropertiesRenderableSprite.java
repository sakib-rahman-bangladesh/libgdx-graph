package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class CommonPropertiesRenderableSprite implements RenderableSprite {
    private Vector3 position;
    private WritablePropertyContainer propertyContainer;

    public CommonPropertiesRenderableSprite() {
        this(new Vector3());
    }

    public CommonPropertiesRenderableSprite(Vector3 position) {
        this(position, new PropertyContainerImpl());
    }

    public CommonPropertiesRenderableSprite(Vector3 position, WritablePropertyContainer propertyContainer) {
        this.position = position;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public WritablePropertyContainer getPropertyContainer(String tag) {
        return propertyContainer;
    }
}
