package com.gempukku.libgdx.graph.plugin.models.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class ModelInstanceDataImpl implements ModelInstanceData {
    private Matrix4 worldTransform = new Matrix4();
    private Matrix4[] bones;
    private Material material;
    private VertexAttributes vertexAttributes;
    private MeshRenderer meshRenderer;

    public void setBones(Matrix4[] bones) {
        this.bones = bones;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setVertexAttributes(VertexAttributes vertexAttributes) {
        this.vertexAttributes = vertexAttributes;
    }

    public void setMeshRenderer(MeshRenderer meshRenderer) {
        this.meshRenderer = meshRenderer;
    }

    @Override
    public Matrix4 getWorldTransform() {
        return worldTransform;
    }

    @Override
    public Matrix4[] getBones() {
        return bones;
    }

    @Override
    public Float getMaterialFloatData(long type) {
        FloatAttribute floatAttribute = (FloatAttribute) material.get(type);
        if (floatAttribute == null)
            return null;
        return floatAttribute.value;
    }

    @Override
    public Color getMaterialColorData(long type) {
        ColorAttribute colorAttribute = (ColorAttribute) material.get(type);
        if (colorAttribute == null)
            return null;
        return colorAttribute.color;
    }

    @Override
    public TextureDescriptor<Texture> getMaterialTextureData(long type) {
        TextureAttribute textureAttribute = (TextureAttribute) material.get(type);
        if (textureAttribute == null)
            return null;
        return textureAttribute.textureDescription;
    }

    @Override
    public Vector2 getMaterialUVData(long type) {
        TextureAttribute textureAttribute = (TextureAttribute) material.get(type);
        if (textureAttribute == null)
            return null;
        return new Vector2(textureAttribute.offsetU, textureAttribute.offsetV);
    }

    @Override
    public Vector2 getMaterialUVScaleData(long type) {
        TextureAttribute textureAttribute = (TextureAttribute) material.get(type);
        if (textureAttribute == null)
            return null;
        return new Vector2(textureAttribute.scaleU, textureAttribute.scaleV);
    }

    @Override
    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    @Override
    public void render(ShaderProgram shaderProgram, int[] attributeLocations) {
        meshRenderer.render(shaderProgram, attributeLocations);
    }

    public interface MeshRenderer {
        void render(ShaderProgram shaderProgram, int[] attributeLocations);
    }
}
