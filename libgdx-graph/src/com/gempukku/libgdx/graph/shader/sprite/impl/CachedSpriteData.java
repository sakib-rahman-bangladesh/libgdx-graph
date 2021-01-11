package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.shader.sprite.SpriteData;

public class CachedSpriteData implements SpriteData {
    private final int spriteCapacity;
    private final int floatCountPerVertex;
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<String, PropertySource> shaderProperties;
    private final IntMap<String> propertyIndexNames;

    private final GraphSpriteImpl[] graphSpritesPosition;
    private VertexBufferObject vbo;
    private IndexBufferObject ibo;

    private float[] vertexData;
    private int spriteCount = 0;
    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    private ObjectSet<GraphSpriteImpl> updatedSprites = new ObjectSet<>();

    public CachedSpriteData(boolean staticCache, int spriteCapacity, int floatCountPerVertex,
                            VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> shaderProperties,
                            IntMap<String> propertyIndexNames) {
        this.spriteCapacity = spriteCapacity;
        this.floatCountPerVertex = floatCountPerVertex;
        this.vertexAttributes = vertexAttributes;
        this.shaderProperties = shaderProperties;
        this.propertyIndexNames = propertyIndexNames;

        graphSpritesPosition = new GraphSpriteImpl[spriteCapacity];
        vertexData = new float[4 * floatCountPerVertex * spriteCapacity];

        vbo = new VertexBufferObject(staticCache, 4 * spriteCapacity, this.vertexAttributes);
        float[] vertices = new float[4 * spriteCapacity * floatCountPerVertex];
        vbo.setVertices(vertices, 0, vertices.length);

        int numberOfIndices = 6 * spriteCapacity;
        ibo = new IndexBufferObject(false, numberOfIndices);
        short[] indices = new short[numberOfIndices];
        int vertexIndex = 0;
        for (int i = 0; i < numberOfIndices; i += 6) {
            indices[i + 0] = (short) (vertexIndex * 4 + 0);
            indices[i + 1] = (short) (vertexIndex * 4 + 2);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            indices[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
        ibo.setIndices(indices, 0, indices.length);
    }

    public boolean addGraphSprite(GraphSpriteImpl sprite) {
        if (spriteCount == spriteCapacity)
            return false;

        graphSpritesPosition[spriteCount] = sprite;

        updatedSprites.add(sprite);

        spriteCount++;

        return true;
    }

    public void updateGraphSprite(GraphSpriteImpl sprite) {
        updatedSprites.add(sprite);
    }

    public boolean removeGraphSprite(GraphSpriteImpl sprite) {
        int position = findSpriteIndex(sprite);
        if (position == -1)
            return false;

        updatedSprites.remove(sprite);

        if (spriteCount > 1 && position != spriteCount - 1) {
            // Need to shrink the arrays
            graphSpritesPosition[position] = graphSpritesPosition[spriteCount - 1];
            int sourcePosition = getSpriteDataStart(spriteCount - 1);
            int destinationPosition = getSpriteDataStart(position);
            int floatCount = floatCountPerVertex * 4;
            System.arraycopy(vertexData, sourcePosition, vertexData, destinationPosition, floatCount);

            markSpriteUpdated(position);
        }
        spriteCount--;

        return true;
    }

    private void updateSpriteData(GraphSpriteImpl sprite, int spriteIndex) {
        int spriteDataStart = getSpriteDataStart(spriteIndex);

        for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
            int floatIndex = 0;
            int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

            for (VertexAttribute vertexAttribute : vertexAttributes) {
                String alias = vertexAttribute.alias;
                if (alias.equals("a_position")) {
                    Vector2 position = sprite.getPosition();
                    vertexData[vertexOffset + floatIndex + 0] = position.x;
                    vertexData[vertexOffset + floatIndex + 1] = position.y;
                    floatIndex += 2;
                } else if (alias.equals("a_layer")) {
                    float layer = sprite.getLayer();
                    vertexData[vertexOffset + floatIndex] = layer;
                    floatIndex += 1;
                } else if (alias.equals("a_size")) {
                    Vector2 size = sprite.getSize();
                    vertexData[vertexOffset + floatIndex + 0] = size.x;
                    vertexData[vertexOffset + floatIndex + 1] = size.y;
                    floatIndex += 2;
                } else if (alias.equals("a_anchor")) {
                    Vector2 anchor = sprite.getAnchor();
                    vertexData[vertexOffset + floatIndex + 0] = anchor.x;
                    vertexData[vertexOffset + floatIndex + 1] = anchor.y;
                    floatIndex += 2;
                } else if (alias.equals(ShaderProgram.TEXCOORD_ATTRIBUTE + 0)) {
                    vertexData[vertexOffset + floatIndex + 0] = vertexIndex % 2;
                    vertexData[vertexOffset + floatIndex + 1] = (float) (vertexIndex / 2);
                    floatIndex += 2;
                } else if (alias.startsWith("a_property_")) {
                    int propertyIndex = Integer.parseInt(alias.substring(11));
                    String propertyName = propertyIndexNames.get(propertyIndex);
                    PropertySource propertySource = shaderProperties.get(propertyName);
                    if (propertySource.getShaderFieldType() == ShaderFieldType.Float) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Number))
                            value = propertySource.getDefaultValue();
                        vertexData[vertexOffset + floatIndex + 0] = ((Number) value).floatValue();
                        floatIndex += 1;
                    } else if (propertySource.getShaderFieldType() == ShaderFieldType.Vector2) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Vector2))
                            value = propertySource.getDefaultValue();
                        vertexData[vertexOffset + floatIndex + 0] = ((Vector2) value).x;
                        vertexData[vertexOffset + floatIndex + 1] = ((Vector2) value).y;
                        floatIndex += 2;
                    } else if (propertySource.getShaderFieldType() == ShaderFieldType.Vector3) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Vector3))
                            value = propertySource.getDefaultValue();
                        vertexData[vertexOffset + floatIndex + 0] = ((Vector3) value).x;
                        vertexData[vertexOffset + floatIndex + 1] = ((Vector3) value).y;
                        vertexData[vertexOffset + floatIndex + 2] = ((Vector3) value).z;
                        floatIndex += 3;
                    } else if (propertySource.getShaderFieldType() == ShaderFieldType.Vector4) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Color))
                            value = propertySource.getDefaultValue();
                        vertexData[vertexOffset + floatIndex + 0] = ((Color) value).r;
                        vertexData[vertexOffset + floatIndex + 1] = ((Color) value).g;
                        vertexData[vertexOffset + floatIndex + 2] = ((Color) value).b;
                        vertexData[vertexOffset + floatIndex + 3] = ((Color) value).a;
                        floatIndex += 4;
                    } else if (propertySource.getShaderFieldType() == ShaderFieldType.TextureRegion) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof TextureRegion))
                            value = propertySource.getDefaultValue();
                        TextureRegion region = (TextureRegion) value;
                        vertexData[vertexOffset + floatIndex + 0] = region.getU();
                        vertexData[vertexOffset + floatIndex + 1] = region.getV();
                        vertexData[vertexOffset + floatIndex + 2] = region.getU2() - region.getU();
                        vertexData[vertexOffset + floatIndex + 3] = region.getV2() - region.getV();
                        floatIndex += 4;
                    }
                }
            }
        }

        markSpriteUpdated(spriteIndex);
    }

    public void prepareForRender(ShaderContextImpl shaderContext) {
        for (GraphSpriteImpl updatedSprite : updatedSprites) {
            updateSpriteData(updatedSprite, findSpriteIndex(updatedSprite));
        }
        updatedSprites.clear();

        shaderContext.setPropertyContainer(graphSpritesPosition[0].getPropertyContainer());
        if (minUpdatedIndex != Integer.MAX_VALUE) {
            vbo.updateVertices(minUpdatedIndex, vertexData, minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            minUpdatedIndex = Integer.MAX_VALUE;
            maxUpdatedIndex = -1;
        }
    }

    @Override
    public void render(ShaderContextImpl shaderContext, ShaderProgram shaderProgram, int[] attributeLocations) {
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Rendering " + spriteCount + " sprite(s)");
        vbo.bind(shaderProgram, attributeLocations);
        ibo.bind();
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6 * spriteCount, GL20.GL_UNSIGNED_SHORT, 0);
        vbo.unbind(shaderProgram);
        ibo.unbind();
    }

    public boolean hasSprites() {
        return spriteCount > 0;
    }

    public void dispose() {
        vbo.dispose();
        ibo.dispose();
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
        maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
    }

    private int getSpriteDataStart(int spriteIndex) {
        return spriteIndex * floatCountPerVertex * 4;
    }

    private int findSpriteIndex(GraphSpriteImpl sprite) {
        for (int i = 0; i < spriteCount; i++) {
            if (graphSpritesPosition[i] == sprite)
                return i;
        }
        return -1;
    }

    public boolean hasSprite(GraphSpriteImpl graphSprite) {
        return findSpriteIndex(graphSprite) != -1;
    }
}
