package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ParticlesGraphShader extends GraphShader {
    private int maxNumberOfParticles;

    public ParticlesGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public void setMaxNumberOfParticles(int maxNumberOfParticles) {
        this.maxNumberOfParticles = maxNumberOfParticles;
    }
}
