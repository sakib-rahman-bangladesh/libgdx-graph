package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.sprite.SpriteProducer;
import com.gempukku.libgdx.graph.sprite.def.SpriteDef;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class RenderingSystem extends EntitySystem implements SpriteProducer.TextureLoader, Disposable, EntityListener {
    private ObjectMap<String, Texture> textures = new ObjectMap<>();
    private TimeProvider timeProvider;
    private PipelineRenderer pipelineRenderer;
    private ImmutableArray<Entity> spriteEntities;

    public RenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        super(priority);
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
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

        SpriteDef spriteDef = spriteComponent.getSpriteDef();

        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();
        GraphSprite graphSprite = graphSprites.createSprite(spriteDef.getLayer(), spriteDef.getTags());
        spriteComponent.setGraphSprite(graphSprite);

        Sprite sprite = SpriteProducer.createSprite(entity, this, graphSprite, spriteDef);
        spriteComponent.setSprite(sprite);
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
        Texture texture = textures.get(path);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(path));
            textures.put(path, texture);
        }
        return texture;
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
    }
}
