package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffect implements Disposable {
    private final static int MAX_NUMBER_OF_PARTICLES_PER_CONTAINER = 256 * 256 / 4 - 1;

    private String tag;
    private ParticleEffectConfiguration particleEffectConfiguration;
    private ParticleGenerator particleGenerator;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();
    private boolean running = false;

    private Array<ParticlesDataContainer> particlesData = new Array<>();
    private int nextParticleIndex = 0;

    private float lastParticleGenerated;

    public GraphParticleEffect(String tag, ParticleEffectConfiguration particleEffectConfiguration, ParticleGenerator particleGenerator,
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

    public String getTag() {
        return tag;
    }

    private void initializeBuffers(ParticleEffectConfiguration particleEffectConfiguration, boolean storeParticleData) {
        int particlesToCreate = particleEffectConfiguration.getMaxNumberOfParticles();
        while (particlesToCreate > 0) {
            int containerCount = Math.min(particlesToCreate, MAX_NUMBER_OF_PARTICLES_PER_CONTAINER);
            particlesData.add(new ParticlesDataContainer(containerCount, storeParticleData));
            particlesToCreate -= containerCount;
        }
    }

    public void generateParticles(TimeProvider timeProvider) {
        if (running) {
            float currentTime = timeProvider.getTime();
            if (lastParticleGenerated == -1) {
                // This is first invocation after start
                int particlesToGenerate = Math.min(particleEffectConfiguration.getInitialParticles(), particleEffectConfiguration.getMaxNumberOfParticles());

                for (ParticlesDataContainer particlesDatum : particlesData) {
                    int numberOfParticles = particlesDatum.getNumberOfParticles();
                    int toGenerate = Math.min(particlesToGenerate, numberOfParticles);
                    for (int i = 0; i < toGenerate; i++) {
                        particlesDatum.generateParticle(currentTime, particleGenerator);
                    }
                    particlesToGenerate -= toGenerate;
                    if (particlesToGenerate == 0)
                        break;
                }
                nextParticleIndex = (nextParticleIndex + particlesToGenerate) % particleEffectConfiguration.getMaxNumberOfParticles();
                lastParticleGenerated = currentTime;
            } else {
                float timeElapsed = currentTime - lastParticleGenerated;
                float particleDelay = particleEffectConfiguration.getParticleDelay();
                int particleCount = MathUtils.floor(timeElapsed / particleDelay);
                while (particleCount > 0) {
                    ParticlesDataContainer container = findContainerForIndex(nextParticleIndex);
                    int remainingCapacity = container.getRemainingCapacity();
                    int createCount = Math.min(remainingCapacity, particleCount);
                    for (int i = 0; i < createCount; i++) {
                        container.generateParticle(lastParticleGenerated + particleDelay * (i + 1), particleGenerator);
                    }
                    particleCount -= createCount;
                    nextParticleIndex = (nextParticleIndex + createCount) % particleEffectConfiguration.getMaxNumberOfParticles();
                    lastParticleGenerated += particleDelay * createCount;
                }
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
        lastParticleGenerated = -1f;
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
}
