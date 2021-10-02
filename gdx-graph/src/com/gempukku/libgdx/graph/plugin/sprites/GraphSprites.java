package com.gempukku.libgdx.graph.plugin.sprites;

public interface GraphSprites {
    GraphSprite addSprite(String tag, RenderableSprite renderableSprite);

    void updateSprite(GraphSprite sprite);

    void removeSprite(GraphSprite sprite);

    void setGlobalProperty(String tag, String name, Object value);

    void unsetGlobalProperty(String tag, String name);

    Object getGlobalProperty(String tag, String name);
}
