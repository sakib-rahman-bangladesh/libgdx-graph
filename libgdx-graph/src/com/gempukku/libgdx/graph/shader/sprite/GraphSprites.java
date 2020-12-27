package com.gempukku.libgdx.graph.shader.sprite;

public interface GraphSprites {
    GraphSprite createSprite(float layer, String... tags);

    void updateSprite(GraphSprite sprite, SpriteUpdater spriteUpdater);

    void destroySprite(GraphSprite sprite);

    void addTag(GraphSprite sprite, String tag);

    void removeTag(GraphSprite sprite, String tag);

    void setProperty(GraphSprite sprite, String name, Object value);

    void unsetProperty(GraphSprite sprite, String name);

    Object getProperty(GraphSprite sprite, String name);
}
