package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public interface ModelInstanceData {
    Matrix4 getWorldTransform();

    Matrix4[] getBones();

    Float getMaterialFloatData(long type);

    Color getMaterialColorData(long type);

    TextureDescriptor<Texture> getMaterialTextureData(long type);

    Vector2 getMaterialUVData(long type);

    Vector2 getMaterialUVScaleData(long type);

    VertexAttributes getVertexAttributes();

    void render(ShaderProgram shaderProgram, int[] attributeLocations);
}
