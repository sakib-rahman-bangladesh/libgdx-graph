package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.math.Vector2;

public interface GraphSprites {
    String createSprite(Vector2 location, Vector2 anchor);

    void updateSprite(String spriteId, SpriteUpdater spriteUpdater);

    void destroySprite(String spriteId);

    void setProperty(String spriteId, String name, Object value);

    void unsetProperty(String spriteId, String name);

    Object getProperty(String spriteId, String name);
}
