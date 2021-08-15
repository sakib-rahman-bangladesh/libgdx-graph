package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.gempukku.libgdx.graph.shader.AttributeDefinition;

import java.util.Map;

public class ParticleEffectConfiguration {
    private VertexAttributes vertexAttributes;
    private Map<String, AttributeDefinition> additionalAttributes;
    private int maxNumberOfParticles;
    private int initialParticles;
    private float particleDelay;

    public ParticleEffectConfiguration(VertexAttributes vertexAttributes,
                                       Map<String, AttributeDefinition> additionalAttributes,
                                       int maxNumberOfParticles, int initialParticles, float particleDelay) {
        this.vertexAttributes = vertexAttributes;
        this.additionalAttributes = additionalAttributes;
        this.maxNumberOfParticles = maxNumberOfParticles;
        this.initialParticles = initialParticles;
        this.particleDelay = particleDelay;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public Map<String, AttributeDefinition> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public int getInitialParticles() {
        return initialParticles;
    }

    public float getParticleDelay() {
        return particleDelay;
    }
}
