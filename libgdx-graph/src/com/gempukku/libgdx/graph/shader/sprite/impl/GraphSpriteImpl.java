package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;

public class GraphSpriteImpl implements GraphSprite {
    private ObjectSet<String> tags = new ObjectSet<>();
    private Vector3 position = new Vector3();
    private Vector2 anchor = new Vector2();
    private Vector2 size = new Vector2();
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public GraphSpriteImpl(Vector3 position, Vector2 anchor, Vector2 size) {
        this.position.set(position);
        this.anchor.set(anchor);
        this.size.set(size);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector2 getAnchor() {
        return anchor;
    }

    public Vector2 getSize() {
        return size;
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

    public void removeTag(String tag) {
        tags.remove(tag);
    }
}
