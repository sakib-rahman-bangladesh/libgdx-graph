package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ParticlesGraphShader extends GraphShader {
    private int maxNumberOfParticles;
    private int initialParticles;
    private float perSecondParticles;

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
}
