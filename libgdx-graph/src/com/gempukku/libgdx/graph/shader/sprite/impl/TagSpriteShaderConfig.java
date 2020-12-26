package com.gempukku.libgdx.graph.shader.sprite.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.sprite.SpriteData;

public class TagSpriteShaderConfig implements SpriteData, Disposable {
    private static final int NUMBER_OF_SPRITES = 2500;

    private VertexAttributes vertexAttributes;
    private VertexBufferObject vbo;
    private IndexBufferObject ibo;

    private float[] tempVertices;
    private final int floatCount;
    private int spriteCount = 0;

    public TagSpriteShaderConfig(Array<VertexAttribute> vertexAttributes) {
        this.vertexAttributes = new VertexAttributes(vertexAttributes.toArray());

        int fCount = 0;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            fCount += vertexAttribute.numComponents;
        }
        floatCount = fCount;

        tempVertices = new float[4 * floatCount];

        vbo = new VertexBufferObject(false, 4 * NUMBER_OF_SPRITES, this.vertexAttributes);

        int numberOfIndices = 6 * NUMBER_OF_SPRITES;
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

    public void clear() {
        spriteCount = 0;
    }

    public void appendSprite(GraphSprite sprite) {
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
                } else if (alias.equals("a_anchor")) {
                    Vector2 anchor = sprite.getAnchor();
                    tempVertices[vertexIndex * floatCount + floatIndex + 0] = anchor.x;
                    tempVertices[vertexIndex * floatCount + floatIndex + 1] = anchor.y;
                    floatIndex += 2;
                } else if (alias.equals("a_size")) {
                    Vector2 size = sprite.getAnchor();
                    tempVertices[vertexIndex * floatCount + floatIndex + 0] = size.x;
                    tempVertices[vertexIndex * floatCount + floatIndex + 1] = size.y;
                    floatIndex += 2;
                }
            }
        }
        vbo.updateVertices(spriteCount * (4 * floatCount), tempVertices, 0, 4 * floatCount);
        spriteCount++;
    }

    public int getCapacity() {
        return NUMBER_OF_SPRITES;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    @Override
    public void render(ShaderProgram shaderProgram, int[] attributeLocations) {
        vbo.bind(shaderProgram, attributeLocations);
        ibo.bind();
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6 * spriteCount, GL20.GL_UNSIGNED_SHORT, 0);
        vbo.unbind(shaderProgram);
        ibo.unbind();
    }

    @Override
    public void dispose() {
        vbo.dispose();
        ibo.dispose();
    }
}
