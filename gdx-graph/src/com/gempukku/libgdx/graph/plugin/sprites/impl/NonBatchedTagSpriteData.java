package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteData;
import com.gempukku.libgdx.graph.plugin.sprites.ValuePerVertex;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class NonBatchedTagSpriteData implements SpriteData, Disposable {
    private String tag;
    private VertexAttributes vertexAttributes;
    private ObjectMap<String, PropertySource> shaderProperties;
    private IntMap<String> propertyIndexNames = new IntMap<>();
    private Mesh mesh;

    private float[] tempVertices;
    private final int floatCount;
    private PropertyContainer propertyContainer;

    public NonBatchedTagSpriteData(String tag, VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> shaderProperties) {
        this.tag = tag;
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
            int vertexOffset = vertexIndex * floatCount;
            for (VertexAttribute vertexAttribute : vertexAttributes) {
                String alias = vertexAttribute.alias;
                if (alias.equals(ShaderProgram.POSITION_ATTRIBUTE)) {
                    Vector3 position = sprite.getRenderableSprite().getPosition();
                    tempVertices[vertexOffset + floatIndex + 0] = position.x;
                    tempVertices[vertexOffset + floatIndex + 1] = position.y;
                    tempVertices[vertexOffset + floatIndex + 2] = position.z;
                    floatIndex += 3;
                } else if (alias.equals(ShaderProgram.TEXCOORD_ATTRIBUTE + 0)) {
                    tempVertices[vertexOffset + floatIndex + 0] = vertexIndex % 2;
                    tempVertices[vertexOffset + floatIndex + 1] = (float) (vertexIndex / 2);
                    floatIndex += 2;
                } else if (alias.startsWith("a_property_")) {
                    int propertyIndex = Integer.parseInt(alias.substring(11));
                    String propertyName = propertyIndexNames.get(propertyIndex);
                    PropertySource propertySource = shaderProperties.get(propertyName);

                    ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
                    Object value = sprite.getRenderableSprite().getPropertyContainer(tag).getValue(propertyName);
                    if (value instanceof ValuePerVertex) {
                        value = ((ValuePerVertex) value).getValue(vertexIndex);
                        value = propertySource.getValueToUse(value);
                    } else {
                        value = propertySource.getValueToUse(value);
                    }

                    floatIndex += shaderFieldType.setValueInAttributesArray(tempVertices, vertexOffset + floatIndex, value);
                }
            }
        }

        mesh.updateVertices(0, tempVertices, 0, 4 * floatCount);
        propertyContainer = sprite.getRenderableSprite().getPropertyContainer(tag);
    }

    @Override
    public void prepareForRender(ShaderContextImpl shaderContext) {
        shaderContext.setLocalPropertyContainer(propertyContainer);
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
