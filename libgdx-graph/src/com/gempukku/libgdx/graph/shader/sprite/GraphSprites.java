package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface GraphSprites {
    String createSprite(Vector3 position, Vector2 anchor, Vector2 size);

    void updateSprite(String spriteId, SpriteUpdater spriteUpdater);

    void destroySprite(String spriteId);

    void setProperty(String spriteId, String name, Object value);

    void unsetProperty(String spriteId, String name);

    Object getProperty(String spriteId, String name);
}
