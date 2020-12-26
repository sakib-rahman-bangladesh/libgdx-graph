package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.util.IdGenerator;
import com.gempukku.libgdx.graph.util.RandomIdGenerator;

public class GraphSpritesImpl implements GraphSprites {
    private ObjectMap<String, GraphSprite> graphSprites = new ObjectMap<>();
    private IdGenerator idGenerator = new RandomIdGenerator(16);

    private Vector3 tempPosition = new Vector3();
    private Vector2 tempAnchor = new Vector2();
    private Vector2 tempSize = new Vector2();

    private ObjectMap<String, TagSpriteShaderConfig> tagSpriteShaderData = new ObjectMap<>();

    @Override
    public String createSprite(Vector3 position, Vector2 anchor, Vector2 size) {
        String id = idGenerator.generateId();
        graphSprites.put(id, new GraphSprite(position, anchor, size));
        return id;
    }

    @Override
    public void updateSprite(String spriteId, SpriteUpdater spriteUpdater) {
        GraphSprite graphSprite = graphSprites.get(spriteId);
        tempPosition.set(graphSprite.getPosition());
        tempAnchor.set(graphSprite.getAnchor());
        tempSize.set(graphSprite.getSize());

        spriteUpdater.processUpdate(tempPosition, tempAnchor, tempSize);

        graphSprite.getPosition().set(tempPosition);
        graphSprite.getAnchor().set(tempAnchor);
        graphSprite.getSize().set(tempSize);
    }

    @Override
    public void destroySprite(String spriteId) {
        graphSprites.remove(spriteId);
    }

    @Override
    public void setProperty(String spriteId, String name, Object value) {
        graphSprites.get(spriteId).getPropertyContainer().setValue(name, value);
    }

    @Override
    public void unsetProperty(String spriteId, String name) {
        graphSprites.get(spriteId).getPropertyContainer().remove(name);
    }

    @Override
    public Object getProperty(String spriteId, String name) {
        return graphSprites.get(spriteId).getPropertyContainer().getValue(name);
    }

    public boolean hasSpriteWithTag(String tag) {
        for (GraphSprite value : graphSprites.values()) {
            if (value.hasTag(tag))
                return true;
        }
        return false;
    }

    public void render(ShaderContext shaderContext, Array<SpriteGraphShader> shaders) {
        for (SpriteGraphShader shader : shaders) {
            String tag = shader.getTag();
            TagSpriteShaderConfig tagSpriteShaderConfig = tagSpriteShaderData.get(tag);

            int spriteTotal = 0;
            int capacity = tagSpriteShaderConfig.getCapacity();

            tagSpriteShaderConfig.clear();
            for (GraphSprite sprite : graphSprites.values()) {
                if (sprite.hasTag(tag)) {
                    tagSpriteShaderConfig.appendSprite(sprite);
                    spriteTotal++;

                    if (capacity == spriteTotal) {
                        shader.renderSprites(shaderContext, tagSpriteShaderConfig.getVertexAttributes(), tagSpriteShaderConfig);
                        tagSpriteShaderConfig.clear();
                        spriteTotal = 0;
                    }
                }
            }

            if (spriteTotal > 0) {
                shader.renderSprites(shaderContext, tagSpriteShaderConfig.getVertexAttributes(), tagSpriteShaderConfig);
            }
        }
    }

    public void registerTag(String tag, Array<VertexAttribute> vertexAttributes) {
        tagSpriteShaderData.put(tag, new TagSpriteShaderConfig(vertexAttributes));
    }
}
