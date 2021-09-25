package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class ParticlesGraphShader extends GraphShader {
    private int maxNumberOfParticles;

    public ParticlesGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return propertySourceMap;
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public void setMaxNumberOfParticles(int maxNumberOfParticles) {
        this.maxNumberOfParticles = maxNumberOfParticles;
    }

    public void renderParticles(ShaderContext shaderContext, Mesh mesh) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        int[] attributeLocations = getAttributeLocations();
        mesh.bind(program, attributeLocations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, mesh.getMaxIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        mesh.unbind(program, attributeLocations);
    }
}
