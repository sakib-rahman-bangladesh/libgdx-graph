package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.sprite.def.SimpleSpriteDef;
import com.gempukku.libgdx.graph.sprite.def.StateBasedSpriteDef;
import com.gempukku.libgdx.graph.sprite.def.TiledSpriteDef;

public class SpriteComponent implements Component {
    private float layer;
    private String[] tags;
    private String spriteType;

    private StateBasedSpriteDef stateBasedSprite;
    private TiledSpriteDef tiledSprite;
    private SimpleSpriteDef simpleSprite;

    private Sprite sprite;
    private GraphSprite graphSprite;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public GraphSprite getGraphSprite() {
        return graphSprite;
    }

    public void setGraphSprite(GraphSprite graphSprite) {
        this.graphSprite = graphSprite;
    }

    public float getLayer() {
        return layer;
    }

    public String[] getTags() {
        return tags;
    }

    public String getSpriteType() {
        return spriteType;
    }

    public StateBasedSpriteDef getStateBasedSprite() {
        return stateBasedSprite;
    }

    public TiledSpriteDef getTiledSprite() {
        return tiledSprite;
    }

    public SimpleSpriteDef getSimpleSprite() {
        return simpleSprite;
    }
}
