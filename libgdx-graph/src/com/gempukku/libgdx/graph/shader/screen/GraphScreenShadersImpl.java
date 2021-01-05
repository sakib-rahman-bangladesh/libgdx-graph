package com.gempukku.libgdx.graph.shader.screen;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class GraphScreenShadersImpl implements GraphScreenShaders {
    private ObjectMap<String, PropertyContainerImpl> propertyContainers = new ObjectMap<>();

    public void registerTag(String tag, ScreenGraphShader shader) {
        if (propertyContainers.containsKey(tag))
            throw new IllegalStateException("Duplicate screen shader with tag - " + tag);
        propertyContainers.put(tag, shader.getPropertyContainer());
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
