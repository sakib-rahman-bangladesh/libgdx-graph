package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class TiledSprite implements Sprite {
    private GraphSprite graphSprite;
    private TextureRegion textureRegion;
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2();
    private Vector2 anchor = new Vector2();
    private Vector2 tileRepeat = new Vector2();
    private boolean textureDirty = true;
    private boolean attributesDirty = true;

    public TiledSprite(GraphSprite graphSprite, Vector2 position, Vector2 size, Vector2 anchor,
                       TextureRegion textureRegion, Vector2 tileRepeat) {
        this.graphSprite = graphSprite;
        this.textureRegion = textureRegion;
        this.position.set(position);
        this.size.set(size);
        this.anchor.set(anchor);
        this.tileRepeat.set(tileRepeat);
    }

    @Override
    public Vector2 getPosition(Vector2 position) {
        return position.set(this.position);
    }

    @Override
    public Vector2 getSize(Vector2 size) {
        return size.set(this.size);
    }

    @Override
    public Vector2 getAnchor(Vector2 anchor) {
        return anchor.set(this.anchor);
    }

    @Override
    public void setPosition(float x, float y) {
        if (!this.position.epsilonEquals(x, y)) {
            this.position.set(x, y);
            attributesDirty = true;
        }
    }

    @Override
    public boolean isDirty() {
        return attributesDirty || textureDirty;
    }

    @Override
    public void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        if (isDirty()) {
            GraphSprites graphSprites = pipelineRenderer.getGraphSprites();
            if (attributesDirty) {
                graphSprites.updateSprite(graphSprite,
                        new SpriteUpdater() {
                            @Override
                            public float processUpdate(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
                                position.set(TiledSprite.this.position);
                                size.set(TiledSprite.this.size);
                                anchor.set(TiledSprite.this.anchor);
                                return layer;
                            }
                        });
            }
            if (textureDirty) {
                graphSprites.setProperty(graphSprite, "Tile Texture", textureRegion);
                graphSprites.setProperty(graphSprite, "Tile Repeat", tileRepeat);
            }

            attributesDirty = false;
            textureDirty = false;
        }
    }
}
