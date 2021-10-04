package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;

public class GraphSpriteImpl implements GraphSprite {
    private String tag;
    private RenderableSprite renderableSprite;

    public GraphSpriteImpl(String tag, RenderableSprite renderableSprite) {
        this.tag = tag;
        this.renderableSprite = renderableSprite;
    }

    public String getTag() {
        return tag;
    }

    public RenderableSprite getRenderableSprite() {
        return renderableSprite;
    }
}
