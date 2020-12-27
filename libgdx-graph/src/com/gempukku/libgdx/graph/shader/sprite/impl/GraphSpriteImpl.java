package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;

public class GraphSpriteImpl implements GraphSprite {
    private ObjectSet<String> tags = new ObjectSet<>();
    private Vector3 position = new Vector3();
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public GraphSpriteImpl(Vector3 position) {
        this.position.set(position);
    }

    public Vector3 getPosition() {
        return position;
    }

    public PropertyContainerImpl getPropertyContainer() {
        return propertyContainer;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    @Override
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public Iterable<String> getAllTags() {
        return tags;
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }
}
