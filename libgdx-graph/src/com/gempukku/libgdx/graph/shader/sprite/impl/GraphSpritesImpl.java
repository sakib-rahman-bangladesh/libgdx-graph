package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;
import com.gempukku.libgdx.graph.util.IdGenerator;
import com.gempukku.libgdx.graph.util.RandomIdGenerator;

import java.util.Arrays;

public class GraphSpritesImpl implements GraphSprites {
    private ObjectMap<String, GraphSprite> graphSprites = new ObjectMap<>();
    private IdGenerator idGenerator = new RandomIdGenerator(16);

    private Vector3 tempPosition = new Vector3();
    private Vector2 tempAnchor = new Vector2();
    private Vector2 tempSize = new Vector2();

    private ObjectMap<String, TagSpriteShaderConfig> tagSpriteShaderData = new ObjectMap<>();
    // TODO limitation on number of textures of 16
    private int[] processingTextureIds = new int[16];
    private int[] tempTextureIds = new int[16];

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
    public void addTag(String spriteId, String tag) {
        graphSprites.get(spriteId).addTag(tag);
    }

    @Override
    public void removeTag(String spriteId, String tag) {
        graphSprites.get(spriteId).removeTag(tag);
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

    public void render(ShaderContextImpl shaderContext, RenderContext renderContext, Array<SpriteGraphShader> shaders) {
        for (SpriteGraphShader shader : shaders) {
            Array<String> textureUniformNames = shader.getTextureUniformNames();
            Arrays.fill(processingTextureIds, 0, textureUniformNames.size, -1);

            shader.begin(shaderContext, renderContext);

            String tag = shader.getTag();
            TagSpriteShaderConfig tagSpriteShaderConfig = tagSpriteShaderData.get(tag);

            int spriteTotal = 0;
            int capacity = tagSpriteShaderConfig.getCapacity();

            tagSpriteShaderConfig.clear();
            for (GraphSprite sprite : graphSprites.values()) {
                if (sprite.hasTag(tag)) {
                    spriteTotal = switchToNewTexturesIfNeeded(shaderContext, shader, textureUniformNames, tagSpriteShaderConfig, spriteTotal, capacity, sprite);
                    tagSpriteShaderConfig.appendSprite(sprite);
                    spriteTotal++;
                }
            }

            if (spriteTotal > 0) {
                shader.renderSprites(shaderContext, tagSpriteShaderConfig);
            }
            shader.end();
        }
    }

    private int switchToNewTexturesIfNeeded(ShaderContextImpl shaderContext, SpriteGraphShader shader, Array<String> textureUniformNames, TagSpriteShaderConfig tagSpriteShaderConfig, int spriteTotal, int capacity, GraphSprite sprite) {
        fetchTextureIds(shader, textureUniformNames, sprite);
        // Not the MOST effective, but good enough
        if (capacity == spriteTotal || !sameValues(processingTextureIds, tempTextureIds, 0, textureUniformNames.size)) {
            if (spriteTotal > 0) {
                shader.renderSprites(shaderContext, tagSpriteShaderConfig);
                tagSpriteShaderConfig.clear();
                spriteTotal = 0;
            }

            shaderContext.setPropertyContainer(sprite.getPropertyContainer());
            System.arraycopy(tempTextureIds, 0, processingTextureIds, 0, textureUniformNames.size);
        }
        return spriteTotal;
    }

    private boolean sameValues(int[] a, int[] b, int start, int count) {
        for (int i = start; i < start + count; i++) {
            if (a[i] != b[i])
                return false;
        }
        return true;
    }

    private void fetchTextureIds(SpriteGraphShader spriteGraphShader, Array<String> textureUniformNames, GraphSprite graphSprite) {
        for (int i = 0; i < textureUniformNames.size; i++) {
            String propertyName = textureUniformNames.get(i);
            Object value = graphSprite.getPropertyContainer().getValue(propertyName);
            if (!(value instanceof TextureRegion))
                value = spriteGraphShader.getPropertySource(propertyName).getDefaultValue();
            TextureRegion region = (TextureRegion) value;
            tempTextureIds[i] = region.getTexture().getTextureObjectHandle();
        }
    }

    public void registerTag(String tag, VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> shaderProperties) {
        tagSpriteShaderData.put(tag, new TagSpriteShaderConfig(vertexAttributes, shaderProperties));
    }
}
