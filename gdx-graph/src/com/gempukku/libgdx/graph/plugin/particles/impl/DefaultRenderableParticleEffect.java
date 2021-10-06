package com.gempukku.libgdx.graph.plugin.particles.impl;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.RenderableParticleEffect;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class DefaultRenderableParticleEffect<T> implements RenderableParticleEffect<T> {
    private ParticleGenerator<T> particleGenerator;
    private WritablePropertyContainer propertyContainer;

    public DefaultRenderableParticleEffect(ParticleGenerator<T> particleGenerator) {
        this(particleGenerator, new PropertyContainerImpl());
    }

    public DefaultRenderableParticleEffect(ParticleGenerator<T> particleGenerator, WritablePropertyContainer propertyContainer) {
        this.particleGenerator = particleGenerator;
        this.propertyContainer = propertyContainer;
    }

    public void setParticleGenerator(ParticleGenerator<T> particleGenerator) {
        this.particleGenerator = particleGenerator;
    }

    @Override
    public boolean isRendered(Camera camera, String tag) {
        return true;
    }

    @Override
    public WritablePropertyContainer getPropertyContainer(String tag) {
        return propertyContainer;
    }

    @Override
    public ParticleGenerator<T> getParticleGenerator(String tag) {
        return particleGenerator;
    }
}
