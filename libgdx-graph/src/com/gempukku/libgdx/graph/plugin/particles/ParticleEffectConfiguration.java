package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class ParticleEffectConfiguration {
    private VertexAttributes vertexAttributes;
    private ObjectMap<String, PropertySource> properties;
    private int maxNumberOfParticles;
    private int initialParticles;
    private float particleDelay;

    public ParticleEffectConfiguration(VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> properties,
                                       int maxNumberOfParticles, int initialParticles, float particleDelay) {
        this.vertexAttributes = vertexAttributes;
        this.properties = properties;
        this.maxNumberOfParticles = maxNumberOfParticles;
        this.initialParticles = initialParticles;
        this.particleDelay = particleDelay;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return properties;
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
