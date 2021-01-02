package com.gempukku.libgdx.graph.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.FacingComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class StateBasedSprite implements Sprite {
    private String state;
    private ObjectMap<String, SpriteStateData> statesData;
    private boolean animationDirty = true;
    private Entity entity;

    public StateBasedSprite(Entity entity, String state, ObjectMap<String, SpriteStateData> statesData) {
        this.entity = entity;
        this.state = state;
        this.statesData = statesData;
    }

    public void setState(String state) {
        if (!statesData.containsKey(state))
            throw new IllegalArgumentException("Undefined state for the sprite");
        if (!this.state.equals(state)) {
            animationDirty = true;
            this.state = state;
        }
    }

    @Override
    public void updateSprite(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        final FacingComponent facingComponent = entity.getComponent(FacingComponent.class);
        boolean attributeDirty = positionComponent.isDirty() || facingComponent.isDirty();

        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        SpriteStateData spriteStateData = statesData.get(state);

        if (attributeDirty) {
            final SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
            final AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);

            graphSprites.updateSprite(spriteComponent.getGraphSprite(),
                    new SpriteUpdater() {
                        @Override
                        public float processUpdate(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
                            position.set(positionComponent.getPosition());
                            SpriteFaceDirection faceDirection = facingComponent.getFaceDirection();
                            size.set(faceDirection.getX(), 1).scl(sizeComponent.getSize());
                            anchor.set(anchorComponent.getAnchor());
                            return layer;
                        }
                    });

            positionComponent.clean();
        }

        if (animationDirty) {
            GraphSprite graphSprite = spriteComponent.getGraphSprite();
            graphSprites.setProperty(graphSprite, "Animated Texture", spriteStateData.sprites);
            graphSprites.setProperty(graphSprite, "Animation Speed", spriteStateData.speed);
            graphSprites.setProperty(graphSprite, "Animation Looping", spriteStateData.looping ? 1f : 0f);
            graphSprites.setProperty(graphSprite, "Sprite Count", new Vector2(spriteStateData.spriteWidth, spriteStateData.spriteHeight));
            graphSprites.setProperty(graphSprite, "Animation Start", timeProvider.getTime());
        }

        animationDirty = false;
    }
}