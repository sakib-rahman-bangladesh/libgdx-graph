package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.math.Vector2;

public interface GraphSprites {
    GraphSprite createSprite(float layer);

    GraphSprite createSprite(float layer, Vector2 position, Vector2 size);

    GraphSprite createSprite(float layer, Vector2 position, Vector2 size, Vector2 anchor);

    void updateSprite(GraphSprite sprite, SpriteUpdater spriteUpdater);

    void destroySprite(GraphSprite sprite);

    void addTag(GraphSprite sprite, String tag);

    void removeTag(GraphSprite sprite, String tag);

    void setProperty(GraphSprite sprite, String name, Object value);

    void unsetProperty(GraphSprite sprite, String name);

    Object getProperty(GraphSprite sprite, String name);
}
