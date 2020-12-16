package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;

public class ParticlesGraphShader extends GraphShader {
    private int maxNumberOfParticles;
    private int initialParticles;
    private float perSecondParticles;
    private int[] attributeLocations;

    public ParticlesGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public void setMaxNumberOfParticles(int maxNumberOfParticles) {
        this.maxNumberOfParticles = maxNumberOfParticles;
    }

    public int getInitialParticles() {
        return initialParticles;
    }

    public void setInitialParticles(int initialParticles) {
        this.initialParticles = initialParticles;
    }

    public float getPerSecondParticles() {
        return perSecondParticles;
    }

    public void setPerSecondParticles(float perSecondParticles) {
        this.perSecondParticles = perSecondParticles;
    }

    public void renderParticles(ShaderContext shaderContext, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        if (attributeLocations == null)
            attributeLocations = getAttributeLocations(vertexBufferObject.getAttributes());

        vertexBufferObject.bind(program, attributeLocations);
        indexBufferObject.bind();
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(program);
        indexBufferObject.unbind();
    }

    private int[] getAttributeLocations(final VertexAttributes attrs) {
        IntArray tempArray = new IntArray();
        final int n = attrs.size();
        for (int i = 0; i < n; i++) {
            Attribute attribute = attributes.get(attrs.get(i).alias);
            if (attribute != null)
                tempArray.add(attribute.getLocation());
            else
                tempArray.add(-1);
        }
        return tempArray.items;
    }
}
