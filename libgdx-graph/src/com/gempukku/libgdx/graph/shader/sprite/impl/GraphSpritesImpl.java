package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;

import java.util.Arrays;

public class GraphSpritesImpl implements GraphSprites {
    private ObjectSet<GraphSpriteImpl> graphSprites = new ObjectSet<>();

    private Vector3 tempPosition = new Vector3();
    private Vector2 tempAnchor = new Vector2();
    private Vector2 tempSize = new Vector2();

    private ObjectMap<String, TagSpriteShaderConfig> tagSpriteShaderData = new ObjectMap<>();
    // TODO limitation on number of textures of 16
    private int[] processingTextureIds = new int[16];
    private int[] tempTextureIds = new int[16];

    @Override
    public GraphSprite createSprite(Vector3 position, Vector2 anchor, Vector2 size) {
        GraphSpriteImpl graphSprite = new GraphSpriteImpl(position, anchor, size);
        graphSprites.add(graphSprite);
        return graphSprite;
    }

    @Override
    public void updateSprite(GraphSprite sprite, SpriteUpdater spriteUpdater) {
        GraphSpriteImpl graphSprite = getSprite(sprite);
        tempPosition.set(graphSprite.getPosition());
        tempAnchor.set(graphSprite.getAnchor());
        tempSize.set(graphSprite.getSize());

        spriteUpdater.processUpdate(tempPosition, tempAnchor, tempSize);

        graphSprite.getPosition().set(tempPosition);
        graphSprite.getAnchor().set(tempAnchor);
        graphSprite.getSize().set(tempSize);
    }

    @Override
    public void destroySprite(GraphSprite sprite) {
        graphSprites.remove(getSprite(sprite));
    }

    @Override
    public void addTag(GraphSprite sprite, String tag) {
        getSprite(sprite).addTag(tag);
    }

    @Override
    public void removeTag(GraphSprite sprite, String tag) {
        getSprite(sprite).removeTag(tag);
    }

    @Override
    public void setProperty(GraphSprite sprite, String name, Object value) {
        getSprite(sprite).getPropertyContainer().setValue(name, value);
    }

    @Override
    public void unsetProperty(GraphSprite sprite, String name) {
        getSprite(sprite).getPropertyContainer().remove(name);
    }

    @Override
    public Object getProperty(GraphSprite sprite, String name) {
        return getSprite(sprite).getPropertyContainer().getValue(name);
    }

    private GraphSpriteImpl getSprite(GraphSprite graphSprite) {
        GraphSpriteImpl spriteImpl = (GraphSpriteImpl) graphSprite;
        if (!graphSprites.contains(spriteImpl))
            throw new IllegalArgumentException("Unable to find the graph sprite");
        return spriteImpl;
    }

    public boolean hasSpriteWithTag(String tag) {
        for (GraphSpriteImpl value : graphSprites) {
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
            for (GraphSpriteImpl sprite : graphSprites) {
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

    private int switchToNewTexturesIfNeeded(ShaderContextImpl shaderContext, SpriteGraphShader shader, Array<String> textureUniformNames, TagSpriteShaderConfig tagSpriteShaderConfig, int spriteTotal, int capacity, GraphSpriteImpl sprite) {
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

    private void fetchTextureIds(SpriteGraphShader spriteGraphShader, Array<String> textureUniformNames, GraphSpriteImpl graphSprite) {
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
