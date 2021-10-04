package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphSpritesImpl implements GraphSprites, RuntimePipelinePlugin, Disposable {
    private int spriteBatchSize;

    private ObjectMap<String, BatchedTagSpriteData> batchedTagSpriteData = new ObjectMap<>();
    private ObjectMap<String, NonBatchedTagSpriteData> nonBatchedTagSpriteData = new ObjectMap<>();

    private ObjectMap<String, ObjectSet<GraphSpriteImpl>> nonBatchedSpritesByTag = new ObjectMap<>();

    private ObjectMap<String, PropertyContainerImpl> tagPropertyContainers = new ObjectMap<>();

    public GraphSpritesImpl(int spriteBatchSize) {
        this.spriteBatchSize = spriteBatchSize;
    }

    @Override
    public GraphSprite addSprite(String tag, RenderableSprite renderableSprite) {
        GraphSpriteImpl graphSprite = new GraphSpriteImpl(tag, renderableSprite);
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null) {
            batchedTagSpriteData.addSprite(graphSprite);
        } else {
            nonBatchedSpritesByTag.get(tag).add(graphSprite);
        }
        return graphSprite;
    }

    @Override
    public void updateSprite(GraphSprite sprite) {
        GraphSpriteImpl graphSprite = getSprite(sprite);
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(graphSprite.getTag());
        if (batchedTagSpriteData != null)
            batchedTagSpriteData.spriteUpdated(graphSprite);
    }

    @Override
    public void removeSprite(GraphSprite sprite) {
        GraphSpriteImpl graphSprite = getSprite(sprite);
        String tag = graphSprite.getTag();
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null)
            batchedTagSpriteData.removeSprite(graphSprite);
        else
            nonBatchedSpritesByTag.get(tag).remove(graphSprite);
    }

    @Override
    public void setGlobalProperty(String tag, String name, Object value) {
        PropertyContainerImpl propertyContainer = tagPropertyContainers.get(tag);
        propertyContainer.setValue(name, value);
    }

    @Override
    public void unsetGlobalProperty(String tag, String name) {
        PropertyContainerImpl propertyContainer = tagPropertyContainers.get(tag);
        propertyContainer.remove(name);
    }

    @Override
    public Object getGlobalProperty(String tag, String name) {
        PropertyContainerImpl propertyContainer = tagPropertyContainers.get(tag);
        return propertyContainer.getValue(name);
    }

    public PropertyContainer getGlobalProperties(String tag) {
        return tagPropertyContainers.get(tag);
    }

    private GraphSpriteImpl getSprite(GraphSprite graphSprite) {
        return (GraphSpriteImpl) graphSprite;
    }

    public boolean hasSpriteWithTag(String tag) {
        BatchedTagSpriteData batchedTagSpriteData = this.batchedTagSpriteData.get(tag);
        if (batchedTagSpriteData != null && batchedTagSpriteData.hasSprites())
            return true;

        return !nonBatchedSpritesByTag.get(tag).isEmpty();
    }

    public Iterable<? extends Array<BatchedSpriteData>> getBatchedSpriteData(String tag) {
        return batchedTagSpriteData.get(tag).getSpriteData();
    }

    public NonBatchedTagSpriteData getNonBatchedSpriteData(String tag) {
        return nonBatchedTagSpriteData.get(tag);
    }

    public Iterable<GraphSpriteImpl> getNonBatchedSprites(String tag) {
        return nonBatchedSpritesByTag.get(tag);
    }
//
//    public void render(ShaderContextImpl shaderContext, RenderContext renderContext,
//                       Array<String> tags, SpriteRenderingStrategy renderingStrategy) {
//        renderingStrategy.processSprites(this, tags, shaderContext.getCamera(),
//                new SpriteRenderingStrategy.StrategyCallback() {
//                    @Override
//                    public void begin() {
//
//                    }
//
//                    @Override
//                    public void process(SpriteData spriteData, String tag) {
//
//                    }
//
//                    @Override
//                    public void end() {
//
//                    }
//                });
//        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//            Gdx.app.debug("Sprite", "Starting drawing sprites");
//        // First - all opaque shaders
//        drawOpaqueSprites(shaderContext, renderContext, opaqueShaders);
//
//        // Then - all the translucent shaders
//        drawTranslucentSprites(shaderContext, renderContext, translucentShaders);
//        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//            Gdx.app.debug("Sprite", "Finished drawing sprites");
//    }

//    private void drawOpaqueSprites(ShaderContextImpl shaderContext, RenderContext renderContext, Array<SpriteGraphShader> opaqueShaders) {
//        for (SpriteGraphShader shader : opaqueShaders) {
//            String tag = shader.getTag();
//            BatchedTagSpriteData dynamicSprites = batchedTagSpriteData.get(tag);
//            if (dynamicSprites.hasSprites()) {
//                if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                    Gdx.app.debug("Sprite", "Starting drawing opaque with tag - " + tag);
//                shaderContext.setGlobalPropertyContainer(tagPropertyContainers.get(tag));
//                shader.begin(shaderContext, renderContext);
//                dynamicSprites.render(shader, shaderContext);
//                shader.end();
//                if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                    Gdx.app.debug("Sprite", "Finished drawing opaque with tag - " + tag);
//            }
//        }
//    }
//
//    private void drawTranslucentSprites(ShaderContextImpl shaderContext, RenderContext renderContext, Array<SpriteGraphShader> translucentShaders) {
//        selectApplicableSpritesForSorting(translucentShaders);
//        distanceSpriteSorter.sort(tempSorting, shaderContext.getCamera().position, Order.Back_To_Front);
//
//        GraphShader lastShader = null;
//        for (GraphSpriteImpl sprite : tempSorting) {
//            for (SpriteGraphShader shader : translucentShaders) {
//                String tag = shader.getTag();
//                if (sprite.hasTag(tag)) {
//                    shaderContext.setLocalPropertyContainer(sprite.getPropertyContainer());
//
//                    NonBatchedTagSpriteData nonCachedTagSpriteData = nonBatchedTagSpriteData.get(tag);
//                    nonCachedTagSpriteData.setSprite(sprite);
//
//                    if (lastShader != shader) {
//                        if (lastShader != null) {
//                            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                                Gdx.app.debug("Sprite", "Finished drawing translucent");
//                            lastShader.end();
//                        }
//                        shaderContext.setGlobalPropertyContainer(tagPropertyContainers.get(tag));
//                        shader.begin(shaderContext, renderContext);
//                        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                            Gdx.app.debug("Sprite", "Starting drawing translucent with tag - " + tag);
//                        lastShader = shader;
//                    }
//                    if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                        Gdx.app.debug("Sprite", "Rendering 1 sprite(s)");
//                    shader.renderSprites(shaderContext, nonCachedTagSpriteData);
//                }
//            }
//        }
//        if (lastShader != null) {
//            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
//                Gdx.app.debug("Sprite", "Finished drawing translucent");
//            lastShader.end();
//        }
//    }
//
//    private void selectApplicableSpritesForSorting(Array<SpriteGraphShader> translucentShaders) {
//        tempSorting.clear();
//        tempForUniqness.clear();
//        for (SpriteGraphShader shader : translucentShaders) {
//            String tag = shader.getTag();
//            ObjectSet<GraphSpriteImpl> graphSprites = nonCachedSpritesByTag.get(tag);
//            if (graphSprites != null) {
//                for (GraphSpriteImpl graphSprite : graphSprites) {
//                    if (tempForUniqness.add(graphSprite))
//                        tempSorting.add(graphSprite);
//                }
//            }
//        }
//    }

    public void registerTag(String tag, SpriteGraphShader shader, boolean batched) {
        if (tagPropertyContainers.containsKey(tag))
            throw new IllegalStateException("There is already a shader with tag: " + tag);

        VertexAttributes vertexAttributes = shader.getVertexAttributes();
        ObjectMap<String, PropertySource> shaderProperties = shader.getProperties();
        Array<String> textureUniformNames = shader.getTextureUniformNames();

        if (batched)
            batchedTagSpriteData.put(tag, new BatchedTagSpriteData(tag, vertexAttributes, spriteBatchSize, shaderProperties, textureUniformNames));
        else {
            nonBatchedTagSpriteData.put(tag, new NonBatchedTagSpriteData(tag, vertexAttributes, shaderProperties));
            nonBatchedSpritesByTag.put(tag, new ObjectSet<GraphSpriteImpl>());
        }

        tagPropertyContainers.put(tag, new PropertyContainerImpl());
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }

    @Override
    public void dispose() {
        for (BatchedTagSpriteData spriteData : batchedTagSpriteData.values()) {
            spriteData.dispose();
        }
        for (NonBatchedTagSpriteData spriteData : nonBatchedTagSpriteData.values()) {
            spriteData.dispose();
        }
    }
}
