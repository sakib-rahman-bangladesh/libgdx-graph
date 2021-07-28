package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteData;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class NonCachedTagSpriteData implements SpriteData, Disposable {
    private VertexAttributes vertexAttributes;
    private ObjectMap<String, PropertySource> shaderProperties;
    private IntMap<String> propertyIndexNames = new IntMap<>();
    private Mesh mesh;

    private float[] tempVertices;
    private final int floatCount;

    public NonCachedTagSpriteData(VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> shaderProperties) {
        this.vertexAttributes = vertexAttributes;
        this.shaderProperties = shaderProperties;

        for (ObjectMap.Entry<String, PropertySource> shaderProperty : shaderProperties) {
            propertyIndexNames.put(shaderProperty.value.getPropertyIndex(), shaderProperty.key);
        }

        int fCount = 0;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            fCount += vertexAttribute.numComponents;
        }
        floatCount = fCount;

        tempVertices = new float[4 * floatCount];
        int numberOfIndices = 6;
        mesh = new Mesh(false, true, 4, numberOfIndices, this.vertexAttributes);
        mesh.setVertices(tempVertices);

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
        mesh.setIndices(indices);
    }

    public void setSprite(GraphSpriteImpl sprite) {
        for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
            int floatIndex = 0;
            for (VertexAttribute vertexAttribute : vertexAttributes) {
                String alias = vertexAttribute.alias;
                if (alias.equals("a_position")) {
                    Vector3 position = sprite.getPosition();
                    tempVertices[vertexIndex * floatCount + floatIndex + 0] = position.x;
                    tempVertices[vertexIndex * floatCount + floatIndex + 1] = position.y;
                    tempVertices[vertexIndex * floatCount + floatIndex + 2] = position.z;
                    floatIndex += 3;
                } else if (alias.equals(ShaderProgram.TEXCOORD_ATTRIBUTE + 0)) {
                    tempVertices[vertexIndex * floatCount + floatIndex + 0] = vertexIndex % 2;
                    tempVertices[vertexIndex * floatCount + floatIndex + 1] = (float) (vertexIndex / 2);
                    floatIndex += 2;
                } else if (alias.startsWith("a_property_")) {
                    int propertyIndex = Integer.parseInt(alias.substring(11));
                    String propertyName = propertyIndexNames.get(propertyIndex);
                    PropertySource propertySource = shaderProperties.get(propertyName);
                    if (propertySource.getShaderFieldType().getName().equals(ShaderFieldType.Float)) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Number))
                            value = propertySource.getDefaultValue();
                        tempVertices[vertexIndex * floatCount + floatIndex + 0] = ((Number) value).floatValue();
                        floatIndex += 1;
                    } else if (propertySource.getShaderFieldType().getName().equals(ShaderFieldType.Vector2)) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Vector2))
                            value = propertySource.getDefaultValue();
                        tempVertices[vertexIndex * floatCount + floatIndex + 0] = ((Vector2) value).x;
                        tempVertices[vertexIndex * floatCount + floatIndex + 1] = ((Vector2) value).y;
                        floatIndex += 2;
                    } else if (propertySource.getShaderFieldType().getName().equals(ShaderFieldType.Vector3)) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Vector3))
                            value = propertySource.getDefaultValue();
                        tempVertices[vertexIndex * floatCount + floatIndex + 0] = ((Vector3) value).x;
                        tempVertices[vertexIndex * floatCount + floatIndex + 1] = ((Vector3) value).y;
                        tempVertices[vertexIndex * floatCount + floatIndex + 2] = ((Vector3) value).z;
                        floatIndex += 3;
                    } else if (propertySource.getShaderFieldType().getName().equals(ShaderFieldType.Vector4)) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof Color))
                            value = propertySource.getDefaultValue();
                        tempVertices[vertexIndex * floatCount + floatIndex + 0] = ((Color) value).r;
                        tempVertices[vertexIndex * floatCount + floatIndex + 1] = ((Color) value).g;
                        tempVertices[vertexIndex * floatCount + floatIndex + 2] = ((Color) value).b;
                        tempVertices[vertexIndex * floatCount + floatIndex + 3] = ((Color) value).a;
                        floatIndex += 4;
                    } else if (propertySource.getShaderFieldType().getName().equals(ShaderFieldType.TextureRegion)) {
                        Object value = sprite.getPropertyContainer().getValue(propertyName);
                        if (!(value instanceof TextureRegion))
                            value = propertySource.getDefaultValue();
                        TextureRegion region = (TextureRegion) value;
                        tempVertices[vertexIndex * floatCount + floatIndex + 0] = region.getU();
                        tempVertices[vertexIndex * floatCount + floatIndex + 1] = region.getV();
                        tempVertices[vertexIndex * floatCount + floatIndex + 2] = region.getU2() - region.getU();
                        tempVertices[vertexIndex * floatCount + floatIndex + 3] = region.getV2() - region.getV();
                        floatIndex += 4;
                    }
                }
            }
        }
        mesh.updateVertices(0, tempVertices, 0, 4 * floatCount);
    }

    @Override
    public void render(ShaderContextImpl shaderContext, ShaderProgram shaderProgram, int[] attributeLocations) {
        mesh.bind(shaderProgram, attributeLocations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6, GL20.GL_UNSIGNED_SHORT, 0);
        mesh.unbind(shaderProgram, attributeLocations);
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
