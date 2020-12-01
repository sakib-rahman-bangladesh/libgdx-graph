package com.gempukku.libgdx.graph.shader.models.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.models.ScreenShaders;

public class ScreenShadersImpl implements ScreenShaders {
    private ObjectMap<String, PropertyContainerImpl> propertyContainers = new ObjectMap<>();

    public void setPropertyContainer(String tag, PropertyContainerImpl propertyContainer) {
        propertyContainers.put(tag, propertyContainer);
    }

    @Override
    public void setProperty(String tag, String name, Object value) {
        PropertyContainerImpl propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        propertyContainer.setValue(name, value);
    }

    @Override
    public void unsetProperty(String tag, String name) {
        PropertyContainerImpl propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        propertyContainer.remove(name);
    }

    @Override
    public Object getProperty(String tag, String name) {
        PropertyContainerImpl propertyContainer = propertyContainers.get(tag);
        if (propertyContainer == null)
            throw new IllegalArgumentException("Screen shader tag not found - " + tag);
        return propertyContainer.getValue(name);
    }
}
