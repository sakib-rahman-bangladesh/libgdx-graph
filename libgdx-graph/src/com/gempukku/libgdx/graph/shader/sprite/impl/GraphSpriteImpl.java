package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;

public class GraphSpriteImpl implements GraphSprite {
    private ObjectSet<String> tags = new ObjectSet<>();
    private float layer;
    private Vector2 position;
    private Vector2 size;
    private Vector2 anchor;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public GraphSpriteImpl(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
        this.layer = layer;
        this.position = position;
        this.size = size;
        this.anchor = anchor;
    }

    public void setLayer(float layer) {
        this.layer = layer;
    }

    public float getLayer() {
        return layer;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getAnchor() {
        return anchor;
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
