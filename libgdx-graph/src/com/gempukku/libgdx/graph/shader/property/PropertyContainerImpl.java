package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;

public class PropertyContainerImpl implements PropertyContainer {
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public void setValue(String name, Object value) {
        properties.put(name, value);
    }

    public void remove(String name) {
        properties.remove(name);
    }

    @Override
    public Object getValue(String name) {
        return properties.get(name);
    }
}
