package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class StateBasedSprite implements FacedSprite {
    private GraphSprite graphSprite;
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2();
    private Vector2 anchor = new Vector2();
    private SpriteFaceDirection faceDirection;
    private String state;
    private ObjectMap<String, SpriteStateData> statesData;
    private boolean attributesDirty = true;
    private boolean animationDirty = true;

    public StateBasedSprite(GraphSprite graphSprite, Vector2 position, Vector2 size, Vector2 anchor,
                            String state, SpriteFaceDirection faceDirection,
                            ObjectMap<String, SpriteStateData> statesData) {
        this.graphSprite = graphSprite;
        this.position.set(position);
        this.size.set(size);
        this.anchor.set(anchor);
        this.state = state;
        this.faceDirection = faceDirection;
        this.statesData = statesData;
    }

    @Override
    public SpriteFaceDirection getFaceDirection() {
        return faceDirection;
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

    public void setState(String state) {
        if (!statesData.containsKey(state))
            throw new IllegalArgumentException("Undefined state for the sprite");
        if (!this.state.equals(state)) {
            animationDirty = true;
            this.state = state;
        }
    }

    public void setFaceDirection(SpriteFaceDirection faceDirection) {
        if (this.faceDirection != faceDirection) {
            attributesDirty = true;
            this.faceDirection = faceDirection;
        }
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
        return attributesDirty || animationDirty;
    }

    @Override
    public void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        if (isDirty()) {
            SpriteStateData spriteStateData = statesData.get(state);

            GraphSprites graphSprites = pipelineRenderer.getGraphSprites();
            if (attributesDirty) {
                graphSprites.updateSprite(graphSprite,
                        new SpriteUpdater() {
                            @Override
                            public float processUpdate(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
                                position.set(StateBasedSprite.this.position);
                                size.set(faceDirection.getX(), 1).scl(StateBasedSprite.this.size);
                                anchor.set(StateBasedSprite.this.anchor);
                                return layer;
                            }
                        });
            }
            if (animationDirty) {
                graphSprites.setProperty(graphSprite, "Animated Texture", spriteStateData.sprites);
                graphSprites.setProperty(graphSprite, "Animation Speed", spriteStateData.speed);
                graphSprites.setProperty(graphSprite, "Animation Looping", spriteStateData.looping ? 1f : 0f);
                graphSprites.setProperty(graphSprite, "Sprite Count", new Vector2(spriteStateData.spriteWidth, spriteStateData.spriteHeight));
                graphSprites.setProperty(graphSprite, "Animation Start", timeProvider.getTime());
            }

            attributesDirty = false;
            animationDirty = false;
        }
    }
}
