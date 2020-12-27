package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteUpdater;

import java.util.Arrays;
import java.util.Comparator;

public class GraphSpritesImpl implements GraphSprites {
    private enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private static final int NUMBER_OF_SPRITES = 2500;
    private static DistanceSpriteSorter distanceSpriteSorter = new DistanceSpriteSorter();

    private ObjectSet<GraphSpriteImpl> tempForUniqness = new ObjectSet<>();
    private Array<GraphSpriteImpl> tempSorting = new Array<>();
    private ObjectSet<GraphSpriteImpl> graphSprites = new ObjectSet<>();
    private ObjectMap<String, ObjectSet<GraphSpriteImpl>> spritesByTag = new ObjectMap<>();

    private ObjectMap<String, TagSpriteShaderConfig> tagSpriteShaderData = new ObjectMap<>();
    // TODO limitation on number of textures of 16
    private int[] processingTextureIds = new int[16];
    private int[] tempTextureIds = new int[16];

    @Override
    public GraphSprite createSprite(float layer, String... tags) {
        GraphSpriteImpl graphSprite = new GraphSpriteImpl(layer);
        graphSprites.add(graphSprite);
        for (String tag : tags) {
            addTag(graphSprite, tag);
        }

        return graphSprite;
    }

    @Override
    public void updateSprite(GraphSprite sprite, SpriteUpdater spriteUpdater) {
        GraphSpriteImpl graphSprite = getSprite(sprite);
        float updateResult = spriteUpdater.processUpdate(graphSprite.getLayer());
        graphSprite.setLayer(updateResult);
    }

    @Override
    public void destroySprite(GraphSprite sprite) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        for (String tag : spriteImpl.getAllTags()) {
            spritesByTag.get(tag).remove(spriteImpl);
        }
        graphSprites.remove(spriteImpl);
    }

    @Override
    public void addTag(GraphSprite sprite, String tag) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        spriteImpl.addTag(tag);
        spritesByTag.get(tag).add(spriteImpl);
    }

    @Override
    public void removeTag(GraphSprite sprite, String tag) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        spritesByTag.get(tag).remove(spriteImpl);
        spriteImpl.removeTag(tag);
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
        // First - all opaque shaders
        for (SpriteGraphShader shader : shaders) {
            if (shader.getBlending() == BasicShader.Blending.opaque) {
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

        // Then - all the translucent shaders
        tempSorting.clear();
        tempForUniqness.clear();
        for (SpriteGraphShader shader : shaders) {
            if (shader.getBlending() != BasicShader.Blending.opaque) {
                String tag = shader.getTag();
                for (GraphSpriteImpl graphSprite : spritesByTag.get(tag)) {
                    if (tempForUniqness.add(graphSprite))
                        tempSorting.add(graphSprite);
                }
            }
        }

        distanceSpriteSorter.sort(tempSorting, Order.Back_To_Front);
        GraphShader lastShader = null;
        for (GraphSpriteImpl sprite : tempSorting) {
            for (SpriteGraphShader shader : shaders) {
                if (shader.getBlending() != BasicShader.Blending.opaque) {
                    String tag = shader.getTag();
                    if (sprite.hasTag(tag)) {
                        shaderContext.setPropertyContainer(sprite.getPropertyContainer());

                        TagSpriteShaderConfig tagSpriteShaderConfig = tagSpriteShaderData.get(tag);
                        tagSpriteShaderConfig.clear();
                        tagSpriteShaderConfig.appendSprite(sprite);

                        if (lastShader != shader) {
                            if (lastShader != null)
                                lastShader.end();
                            shader.begin(shaderContext, renderContext);
                            lastShader = shader;
                        }
                        shader.renderSprites(shaderContext, tagSpriteShaderConfig);
                    }
                }
            }
        }
        if (lastShader != null)
            lastShader.end();
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

    public void registerTag(String tag, VertexAttributes vertexAttributes, boolean opaque, ObjectMap<String, PropertySource> shaderProperties) {
        tagSpriteShaderData.put(tag, new TagSpriteShaderConfig(vertexAttributes, opaque ? NUMBER_OF_SPRITES : 1, shaderProperties));
        spritesByTag.put(tag, new ObjectSet<GraphSpriteImpl>());
    }

    private static class DistanceSpriteSorter implements Comparator<GraphSpriteImpl> {
        private Order order;

        public void sort(Array<GraphSpriteImpl> sprites, Order order) {
            this.order = order;
            sprites.sort(this);
        }

        @Override
        public int compare(GraphSpriteImpl o1, GraphSpriteImpl o2) {
            final float dst = (int) (1000f * o1.getLayer()) - (int) (1000f * o2.getLayer());
            return order.result(dst);
        }
    }
}
