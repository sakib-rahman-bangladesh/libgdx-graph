package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class DefaultRenderableSprite implements RenderableSprite {
    private Vector3 position;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public DefaultRenderableSprite(Vector3 position) {
        this.position = position;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public PropertyContainerImpl getPropertyContainer() {
        return propertyContainer;
    }
}
