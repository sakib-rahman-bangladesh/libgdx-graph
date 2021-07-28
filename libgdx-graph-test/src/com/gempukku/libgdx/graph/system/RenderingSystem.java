package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.sprite.SpriteProducer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class RenderingSystem extends EntitySystem implements SpriteProducer.TextureLoader, Disposable, EntityListener {
    private TimeProvider timeProvider;
    private PipelineRenderer pipelineRenderer;
    private SpriteProducer.TextureLoader textureLoader;
    private ImmutableArray<Entity> spriteEntities;
    private Vector3 tmpPosition = new Vector3();

    public RenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer,
                           SpriteProducer.TextureLoader textureLoader) {
        super(priority);
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
        this.textureLoader = textureLoader;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family sprite = Family.all(SpriteComponent.class).get();
        spriteEntities = engine.getEntitiesFor(sprite);
        engine.addEntityListener(sprite, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
        GraphSprite graphSprite = graphSprites.createSprite(tmpPosition);
        spriteComponent.setGraphSprite(graphSprite);

        Sprite sprite = SpriteProducer.createSprite(entity, this, graphSprite, spriteComponent);
        sprite.updateSprite(timeProvider, pipelineRenderer);
        spriteComponent.setSprite(sprite);

        for (String tag : spriteComponent.getTags()) {
            graphSprites.addTag(graphSprite, tag);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    @Override
    public void update(float delta) {
        for (Entity spriteEntity : spriteEntities) {
            SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
            sprite.getSprite().updateSprite(timeProvider, pipelineRenderer);
        }
    }


    @Override
    public Texture loadTexture(String path) {
        return textureLoader.loadTexture(path);
    }

    @Override
    public void dispose() {
    }
}
