package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class CachedTagSpriteData implements Disposable {
    private final VertexAttributes vertexAttributes;
    private final int numberOfSpritesPerBatch;
    private final ObjectMap<String, PropertySource> shaderProperties;
    private final Array<String> textureUniformNames;
    private final IntMap<String> propertyIndexNames = new IntMap<>();
    private final int floatCount;

    private ObjectMap<String, Array<CachedSpriteData>> dynamicCachedSpritesPerTextureSet = new ObjectMap<>();

    public CachedTagSpriteData(VertexAttributes vertexAttributes, int numberOfSpritesPerBatch, ObjectMap<String, PropertySource> shaderProperties,
                               Array<String> textureUniformNames) {
        this.vertexAttributes = vertexAttributes;
        this.numberOfSpritesPerBatch = numberOfSpritesPerBatch;
        this.shaderProperties = shaderProperties;
        this.textureUniformNames = textureUniformNames;

        for (ObjectMap.Entry<String, PropertySource> shaderProperty : shaderProperties) {
            propertyIndexNames.put(shaderProperty.value.getPropertyIndex(), shaderProperty.key);
        }

        int fCount = 0;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            fCount += vertexAttribute.numComponents;
        }
        floatCount = fCount;
    }

    public void addSprite(GraphSpriteImpl sprite) {
        String textureSignature = getTextureSignature(sprite);
        Array<CachedSpriteData> dynamicCachedSpriteData = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (dynamicCachedSpriteData == null) {
            dynamicCachedSpriteData = new Array<>();
            dynamicCachedSpritesPerTextureSet.put(textureSignature, dynamicCachedSpriteData);
        }

        for (CachedSpriteData dynamicCachedSprite : dynamicCachedSpriteData) {
            if (dynamicCachedSprite.addGraphSprite(sprite))
                return;
        }
        CachedSpriteData cachedSpriteData = new CachedSpriteData(false, numberOfSpritesPerBatch, floatCount,
                vertexAttributes, shaderProperties, propertyIndexNames);
        dynamicCachedSpriteData.add(cachedSpriteData);
        cachedSpriteData.addGraphSprite(sprite);
    }

    public void spriteUpdated(GraphSpriteImpl graphSprite) {
        CachedSpriteData cachedSpriteData = findCachedSpriteDataContainingSprite(graphSprite);
        if (cachedSpriteData == null) {
            // Texture change
            removeRegardlessOfTexture(graphSprite);
            addSprite(graphSprite);
        } else {
            cachedSpriteData.updateGraphSprite(graphSprite);
        }
    }

    private void removeRegardlessOfTexture(GraphSpriteImpl graphSprite) {
        for (ObjectMap.Entry<String, Array<CachedSpriteData>> dynamicCachedSprites : new ObjectMap.Entries<>(dynamicCachedSpritesPerTextureSet)) {
            for (CachedSpriteData dynamicCachedSprite : dynamicCachedSprites.value) {
                if (dynamicCachedSprite.removeGraphSprite(graphSprite)) {
                    // Cleanup unneeded collections
                    if (!dynamicCachedSprite.hasSprites()) {
                        dynamicCachedSprites.value.removeValue(dynamicCachedSprite, true);
                        dynamicCachedSprite.dispose();

                        if (dynamicCachedSprites.value.size == 0) {
                            dynamicCachedSpritesPerTextureSet.remove(dynamicCachedSprites.key);
                        }
                    }
                    return;
                }
            }
        }
    }

    private CachedSpriteData findCachedSpriteDataContainingSprite(GraphSpriteImpl graphSprite) {
        String textureSignature = getTextureSignature(graphSprite);
        Array<CachedSpriteData> array = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (array != null) {
            for (CachedSpriteData cachedSpriteData : array) {
                if (cachedSpriteData.hasSprite(graphSprite))
                    return cachedSpriteData;
            }
        }

        return null;
    }

    public void removeSprite(GraphSpriteImpl sprite) {
        String textureSignature = getTextureSignature(sprite);
        Array<CachedSpriteData> dynamicCachedSprites = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (dynamicCachedSprites != null) {
            for (CachedSpriteData dynamicCachedSprite : dynamicCachedSprites) {
                if (dynamicCachedSprite.removeGraphSprite(sprite)) {
                    // Cleanup unneeded collections
                    if (!dynamicCachedSprite.hasSprites()) {
                        dynamicCachedSprites.removeValue(dynamicCachedSprite, true);
                        dynamicCachedSprite.dispose();

                        if (dynamicCachedSprites.size == 0) {
                            dynamicCachedSpritesPerTextureSet.remove(textureSignature);
                        }
                    }
                    return;
                }
            }
        }
    }

    public boolean hasSprites() {
        for (Array<CachedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (CachedSpriteData cachedSpriteData : array) {
                if (cachedSpriteData.hasSprites())
                    return true;
            }
        }

        return false;
    }

    public void render(SpriteGraphShader shader, ShaderContextImpl shaderContext) {
        for (Array<CachedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (CachedSpriteData cachedSpriteData : array) {
                cachedSpriteData.prepareForRender(shaderContext);
                shader.renderSprites(shaderContext, cachedSpriteData);
            }
        }
    }

    @Override
    public void dispose() {
        for (Array<CachedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (CachedSpriteData cachedSpriteData : array) {
                cachedSpriteData.dispose();
            }
        }
    }

    private String getTextureSignature(GraphSpriteImpl graphSprite) {
        if (textureUniformNames.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textureUniformNames.size; i++) {
            String propertyName = textureUniformNames.get(i);
            PropertySource propertySource = shaderProperties.get(propertyName);
            Object region = graphSprite.getPropertyContainer().getValue(propertyName);
            region = propertySource.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
