package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteUpdater;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.util.Comparator;

public class GraphSpritesImpl implements GraphSprites, RuntimePipelinePlugin, Disposable {
    private enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private static DistanceSpriteSorter distanceSpriteSorter = new DistanceSpriteSorter();

    private int spriteBatchSize;

    private ObjectSet<GraphSpriteImpl> tempForUniqness = new ObjectSet<>();
    private Array<GraphSpriteImpl> tempSorting = new Array<>();

    private ObjectMap<String, CachedTagSpriteData> dynamicCachedTagSpriteData = new ObjectMap<>();
    private ObjectMap<String, NonCachedTagSpriteData> nonCachedTagSpriteDataObjectMap = new ObjectMap<>();

    private ObjectMap<String, ObjectSet<GraphSpriteImpl>> nonCachedSpritesByTag = new ObjectMap<>();

    private Vector3 tempPosition = new Vector3();
    private Vector2 tempSize = new Vector2();
    private Vector2 tempAnchor = new Vector2();

    public GraphSpritesImpl(int spriteBatchSize) {
        this.spriteBatchSize = spriteBatchSize;
    }

    @Override
    public GraphSprite createSprite(float layer) {
        return createSprite(layer, new Vector2(0, 0), new Vector2(32, 32));
    }

    @Override
    public GraphSprite createSprite(float layer, Vector2 position, Vector2 size) {
        return createSprite(layer, position, size, new Vector2(0.5f, 0.5f));
    }

    @Override
    public GraphSprite createSprite(float layer, Vector2 position, Vector2 size, Vector2 anchor) {
        return new GraphSpriteImpl(layer, position, size, anchor);
    }

    @Override
    public void updateSprite(GraphSprite sprite, SpriteUpdater spriteUpdater) {
        GraphSpriteImpl graphSprite = getSprite(sprite);
        tempPosition.set(graphSprite.getPosition(), graphSprite.getLayer());
        tempSize.set(graphSprite.getSize());
        tempAnchor.set(graphSprite.getAnchor());
        spriteUpdater.processUpdate(tempPosition, tempSize, tempAnchor);
        graphSprite.setLayer(tempPosition.z);
        graphSprite.getPosition().set(tempPosition.x, tempPosition.y);
        graphSprite.getSize().set(tempSize);
        graphSprite.getAnchor().set(tempAnchor);

        spriteUpdated(graphSprite);
    }

    private void spriteUpdated(GraphSpriteImpl graphSprite) {
        for (String tag : graphSprite.getAllTags()) {
            CachedTagSpriteData cachedTagSpriteData = dynamicCachedTagSpriteData.get(tag);
            if (cachedTagSpriteData != null)
                cachedTagSpriteData.spriteUpdated(graphSprite);
        }
    }

    @Override
    public void destroySprite(GraphSprite sprite) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        for (String tag : spriteImpl.getAllTags()) {
            CachedTagSpriteData cachedTagSpriteData = dynamicCachedTagSpriteData.get(tag);
            if (cachedTagSpriteData != null)
                cachedTagSpriteData.removeSprite(spriteImpl);
            ObjectSet<GraphSpriteImpl> nonCachedGraphSprites = nonCachedSpritesByTag.get(tag);
            if (nonCachedGraphSprites != null)
                nonCachedGraphSprites.remove(spriteImpl);
        }
    }

    @Override
    public void addTag(GraphSprite sprite, String tag) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        spriteImpl.addTag(tag);

        CachedTagSpriteData cachedTagSpriteData = dynamicCachedTagSpriteData.get(tag);
        if (cachedTagSpriteData != null) {
            cachedTagSpriteData.addSprite(spriteImpl);
        } else {
            ObjectSet<GraphSpriteImpl> graphSprites = nonCachedSpritesByTag.get(tag);
            if (graphSprites == null) {
                graphSprites = new ObjectSet<>();
                nonCachedSpritesByTag.put(tag, graphSprites);
            }
            graphSprites.add(spriteImpl);
        }
    }

    @Override
    public void removeTag(GraphSprite sprite, String tag) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        CachedTagSpriteData cachedTagSpriteData = dynamicCachedTagSpriteData.get(tag);
        if (cachedTagSpriteData != null) {
            cachedTagSpriteData.removeSprite(spriteImpl);
        } else {
            ObjectSet<GraphSpriteImpl> graphSprites = nonCachedSpritesByTag.get(tag);
            graphSprites.remove(spriteImpl);
            if (graphSprites.isEmpty())
                nonCachedSpritesByTag.remove(tag);
        }
        spriteImpl.removeTag(tag);
    }

    @Override
    public void setProperty(GraphSprite sprite, String name, Object value) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        spriteImpl.getPropertyContainer().setValue(name, value);
        spriteUpdated(spriteImpl);
    }

    @Override
    public void unsetProperty(GraphSprite sprite, String name) {
        GraphSpriteImpl spriteImpl = getSprite(sprite);
        spriteImpl.getPropertyContainer().remove(name);
        spriteUpdated(spriteImpl);
    }

    @Override
    public Object getProperty(GraphSprite sprite, String name) {
        return getSprite(sprite).getPropertyContainer().getValue(name);
    }

    private GraphSpriteImpl getSprite(GraphSprite graphSprite) {
        return (GraphSpriteImpl) graphSprite;
    }

    public boolean hasSpriteWithTag(String tag) {
        CachedTagSpriteData cachedTagSpriteData = dynamicCachedTagSpriteData.get(tag);
        if (cachedTagSpriteData != null && cachedTagSpriteData.hasSprites())
            return true;

        return !nonCachedSpritesByTag.get(tag).isEmpty();
    }

    public void render(ShaderContextImpl shaderContext, RenderContext renderContext,
                       Array<SpriteGraphShader> opaqueShaders, Array<SpriteGraphShader> translucentShaders) {
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Starting drawing sprites");
        // First - all opaque shaders
        drawOpaqueSprites(shaderContext, renderContext, opaqueShaders);

        // Then - all the translucent shaders
        drawTranslucentSprites(shaderContext, renderContext, translucentShaders);
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Finished drawing sprites");
    }

    private void drawOpaqueSprites(ShaderContextImpl shaderContext, RenderContext renderContext, Array<SpriteGraphShader> opaqueShaders) {
        for (SpriteGraphShader shader : opaqueShaders) {
            String tag = shader.getTag();
            CachedTagSpriteData dynamicSprites = dynamicCachedTagSpriteData.get(tag);
            if (dynamicSprites.hasSprites()) {
                if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                    Gdx.app.debug("Sprite", "Starting drawing opaque with tag - " + tag);
                shader.begin(shaderContext, renderContext);
                dynamicSprites.render(shader, shaderContext);
                shader.end();
                if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                    Gdx.app.debug("Sprite", "Finished drawing opaque with tag - " + tag);
            }
        }
    }

    private void drawTranslucentSprites(ShaderContextImpl shaderContext, RenderContext renderContext, Array<SpriteGraphShader> translucentShaders) {
        selectApplicableSpritesForSorting(translucentShaders);
        distanceSpriteSorter.sort(tempSorting, Order.Back_To_Front);

        GraphShader lastShader = null;
        for (GraphSpriteImpl sprite : tempSorting) {
            for (SpriteGraphShader shader : translucentShaders) {
                String tag = shader.getTag();
                if (sprite.hasTag(tag)) {
                    shaderContext.setPropertyContainer(sprite.getPropertyContainer());

                    NonCachedTagSpriteData nonCachedTagSpriteData = nonCachedTagSpriteDataObjectMap.get(tag);
                    nonCachedTagSpriteData.setSprite(sprite);

                    if (lastShader != shader) {
                        if (lastShader != null) {
                            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                                Gdx.app.debug("Sprite", "Finished drawing translucent");
                            lastShader.end();
                        }
                        shader.begin(shaderContext, renderContext);
                        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                            Gdx.app.debug("Sprite", "Starting drawing translucent with tag - " + tag);
                        lastShader = shader;
                    }
                    if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                        Gdx.app.debug("Sprite", "Rendering 1 sprite(s)");
                    shader.renderSprites(shaderContext, nonCachedTagSpriteData);

                }
            }
        }
        if (lastShader != null) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("Sprite", "Finished drawing translucent");
            lastShader.end();
        }
    }

    private void selectApplicableSpritesForSorting(Array<SpriteGraphShader> translucentShaders) {
        tempSorting.clear();
        tempForUniqness.clear();
        for (SpriteGraphShader shader : translucentShaders) {
            String tag = shader.getTag();
            ObjectSet<GraphSpriteImpl> graphSprites = nonCachedSpritesByTag.get(tag);
            if (graphSprites != null) {
                for (GraphSpriteImpl graphSprite : graphSprites) {
                    if (tempForUniqness.add(graphSprite))
                        tempSorting.add(graphSprite);
                }
            }
        }
    }

    public void registerTag(String tag, SpriteGraphShader shader) {
        boolean opaque = (shader.getBlending() == BasicShader.Blending.opaque);
        VertexAttributes vertexAttributes = shader.getVertexAttributes();
        ObjectMap<String, PropertySource> shaderProperties = shader.getProperties();
        Array<String> textureUniformNames = shader.getTextureUniformNames();

        if (opaque)
            dynamicCachedTagSpriteData.put(tag, new CachedTagSpriteData(vertexAttributes, spriteBatchSize, shaderProperties, textureUniformNames));
        else
            nonCachedTagSpriteDataObjectMap.put(tag, new NonCachedTagSpriteData(vertexAttributes, shaderProperties));
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }

    @Override
    public void dispose() {
        for (CachedTagSpriteData spriteData : dynamicCachedTagSpriteData.values()) {
            spriteData.dispose();
        }
        for (NonCachedTagSpriteData spriteData : nonCachedTagSpriteDataObjectMap.values()) {
            spriteData.dispose();
        }
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
