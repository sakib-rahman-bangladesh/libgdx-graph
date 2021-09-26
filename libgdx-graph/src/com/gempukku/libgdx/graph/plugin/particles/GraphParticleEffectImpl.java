package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffectImpl implements GraphParticleEffect, Disposable {
    private final static int MAX_NUMBER_OF_PARTICLES_PER_CONTAINER = 256 * 256 / 4 - 1;

    private String tag;
    private ParticleEffectConfiguration particleEffectConfiguration;
    private ParticleGenerator particleGenerator;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();
    private ParticleCreateCallbackImpl callback = new ParticleCreateCallbackImpl();
    private boolean running = false;

    private Array<ParticlesDataContainer> particlesData = new Array<>();
    private int nextParticleIndex = 0;

    private boolean initialParticles;

    public GraphParticleEffectImpl(String tag, ParticleEffectConfiguration particleEffectConfiguration, ParticleGenerator particleGenerator,
                                   boolean storeParticleData) {
        this.tag = tag;
        this.particleEffectConfiguration = particleEffectConfiguration;
        this.particleGenerator = particleGenerator;

        initializeBuffers(particleEffectConfiguration, storeParticleData);
    }

    public void setParticleGenerator(ParticleGenerator particleGenerator) {
        this.particleGenerator = particleGenerator;
    }

    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public String getTag() {
        return tag;
    }

    private void initializeBuffers(ParticleEffectConfiguration particleEffectConfiguration, boolean storeParticleData) {
        int particlesToCreate = particleEffectConfiguration.getMaxNumberOfParticles();
        while (particlesToCreate > 0) {
            int containerCount = Math.min(particlesToCreate, MAX_NUMBER_OF_PARTICLES_PER_CONTAINER);
            particlesData.add(new ParticlesDataContainer(particleEffectConfiguration.getVertexAttributes(),
                    particleEffectConfiguration.getProperties(),
                    containerCount, storeParticleData));
            particlesToCreate -= containerCount;
        }
    }

    public void generateParticles(TimeProvider timeProvider) {
        if (running) {
            if (initialParticles) {
                particleGenerator.initialCreateParticles(callback);
                initialParticles = false;
            } else {
                particleGenerator.createParticles(callback);
            }
        }
    }

    private ParticlesDataContainer findContainerForIndex(int particleIndex) {
        int skipped = 0;
        for (ParticlesDataContainer particlesDatum : particlesData) {
            int numberOfParticles = particlesDatum.getNumberOfParticles();
            if (numberOfParticles > particleIndex - skipped) {
                return particlesDatum;
            } else {
                skipped += numberOfParticles;
            }
        }
        return null;
    }

    public void render(ParticlesGraphShader graphShader, ShaderContext shaderContext) {
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.applyPendingChanges();
        }
        float currentTime = shaderContext.getTimeProvider().getTime();
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.render(graphShader, shaderContext, currentTime);
        }
    }

    public void start() {
        if (running)
            throw new IllegalStateException("Already started");
        running = true;
        initialParticles = true;
    }

    public void update(TimeProvider timeProvider, ParticleUpdater particleUpdater, boolean accessToData) {
        float currentTime = timeProvider.getTime();
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.update(particleUpdater, currentTime, accessToData);
        }
    }

    public void stop() {
        if (!running)
            throw new IllegalStateException("Not started");
        running = false;
    }

    public void setProperty(String name, Object value) {
        propertyContainer.setValue(name, value);
    }

    public void unsetProperty(String name) {
        propertyContainer.remove(name);
    }

    public Object getProperty(String name) {
        return propertyContainer.getValue(name);
    }

    @Override
    public void dispose() {
        for (ParticlesDataContainer particlesDatum : particlesData) {
            particlesDatum.dispose();
        }
    }

    private class ParticleCreateCallbackImpl<T> implements ParticleGenerator.ParticleCreateCallback<T> {
        @Override
        public void createParticle(float particleBirth, float lifeLength, ObjectMap<String, Object> attributes) {
            createParticle(particleBirth, lifeLength, null, attributes);
        }

        @Override
        public void createParticle(float particleBirth, float lifeLength, T particleData, ObjectMap<String, Object> attributes) {
            ParticlesDataContainer container = findContainerForIndex(nextParticleIndex);
            container.generateParticle(particleBirth, lifeLength, particleData, attributes);

            nextParticleIndex = (nextParticleIndex + 1) % particleEffectConfiguration.getMaxNumberOfParticles();
        }
    }
}
