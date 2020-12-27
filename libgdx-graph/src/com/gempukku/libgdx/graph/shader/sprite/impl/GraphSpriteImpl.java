package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;

public class GraphSpriteImpl implements GraphSprite {
    private ObjectSet<String> tags = new ObjectSet<>();
    private float layer;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public GraphSpriteImpl(float layer) {
        this.layer = layer;
    }

    public void setLayer(float layer) {
        this.layer = layer;
    }

    public float getLayer() {
        return layer;
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
