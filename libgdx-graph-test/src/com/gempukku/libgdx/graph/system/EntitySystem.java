package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.entity.GameEntity;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class EntitySystem implements GameSystem {
    private TimeProvider timeProvider;
    private PipelineRenderer pipelineRenderer;
    private ObjectSet<GameEntity<?>> gameEntities = new ObjectSet<>();

    public EntitySystem(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
    }

    public <T extends Sprite> GameEntity<T> createGameEntity(T sprite) {
        GameEntity<T> entity = new GameEntity<>(sprite);
        gameEntities.add(entity);
        return entity;
    }

    @Override
    public void update(float delta) {
        for (GameEntity<?> gameEntity : gameEntities) {
            if (gameEntity.isDynamic()) {
                Body body = gameEntity.getBody();
                gameEntity.getSprite().setPosition(body.getPosition().x * PhysicsSystem.PIXELS_TO_METERS, body.getPosition().y * PhysicsSystem.PIXELS_TO_METERS);
            }
        }
        for (GameEntity<?> gameEntity : gameEntities) {
            Sprite sprite = gameEntity.getSprite();
            if (sprite.isDirty())
                sprite.updateSprite(timeProvider, pipelineRenderer);
        }

    }

    @Override
    public void dispose() {

    }
}
