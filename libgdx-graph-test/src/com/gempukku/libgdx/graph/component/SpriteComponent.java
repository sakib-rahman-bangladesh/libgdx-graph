package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.sprite.def.SpriteDef;

public class SpriteComponent implements Component {
    private SpriteDef spriteDef;
    private Sprite sprite;
    private GraphSprite graphSprite;

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public SpriteDef getSpriteDef() {
        return spriteDef;
    }

    public void setSpriteDef(SpriteDef spriteDef) {
        this.spriteDef = spriteDef;
    }

    public GraphSprite getGraphSprite() {
        return graphSprite;
    }

    public void setGraphSprite(GraphSprite graphSprite) {
        this.graphSprite = graphSprite;
    }
}
