package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.time.TimeProvider;
import com.gempukku.libgdx.graph.util.RandomIdGenerator;

public class GraphParticleEffectsImpl implements GraphParticleEffects, Disposable {
    private ObjectMap<String, GraphParticleEffect> particleEffects = new ObjectMap<>();
    private ObjectMap<String, ParticleEffectConfiguration> effectsConfiguration = new ObjectMap<>();
    private RandomIdGenerator randomIdGenerator = new RandomIdGenerator(16);
    private TimeProvider timeProvider;

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void registerEffect(String tag, VertexAttributes vertexAttributes, int maxNumberOfParticles, int initialParticles, float particlesPerSecond) {
        if (effectsConfiguration.containsKey(tag))
            throw new IllegalStateException("Duplicate particle effect with tag - " + tag);
        effectsConfiguration.put(tag, new ParticleEffectConfiguration(vertexAttributes, maxNumberOfParticles, initialParticles, 1f / particlesPerSecond));
    }

    public ObjectMap.Values<GraphParticleEffect> getParticleEffects() {
        return particleEffects.values();
    }

    @Override
    public String createEffect(String tag, ParticleGenerator particleGenerator) {
        return createEffect(tag, particleGenerator, null);
    }

    @Override
    public <T> String createEffect(String tag, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass) {
        ParticleEffectConfiguration configuration = effectsConfiguration.get(tag);
        if (configuration == null)
            throw new IllegalArgumentException("Unable to find particle effect with tag - " + tag);

        String effectId = randomIdGenerator.generateId();
        GraphParticleEffect particleEffect = new GraphParticleEffect(tag, configuration, particleGenerator, particleDataClass != null);
        particleEffects.put(effectId, particleEffect);

        return effectId;
    }

    @Override
    public void setGenerator(String effectId, ParticleGenerator particleGenerator) {
        setGenerator(effectId, particleGenerator, null);
    }

    @Override
    public <T> void setGenerator(String effectId, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass) {
        GraphParticleEffect particleEffect = particleEffects.get(effectId);
        particleEffect.setParticleGenerator(particleGenerator);
    }

    @Override
    public void startEffect(String effectId) {
        particleEffects.get(effectId).start();
    }

    @Override
    public void updateParticles(String effectId, ParticleUpdater particleUpdater) {
        updateParticles(effectId, particleUpdater, null);
    }

    @Override
    public <T> void updateParticles(String effectId, ParticleUpdater<T> particleUpdater, Class<T> particleDataClass) {
        particleEffects.get(effectId).update(timeProvider, particleUpdater, particleDataClass != null);
    }

    @Override
    public void stopEffect(String effectId) {
        particleEffects.get(effectId).stop();
    }

    @Override
    public void destroyEffect(String effectId) {
        GraphParticleEffect effect = particleEffects.remove(effectId);
        effect.dispose();
    }

    @Override
    public void setProperty(String effectId, String name, Object value) {
        particleEffects.get(effectId).setProperty(name, value);
    }

    @Override
    public void unsetProperty(String effectId, String name) {
        particleEffects.get(effectId).unsetProperty(name);
    }

    @Override
    public Object getProperty(String effectId, String name) {
        return particleEffects.get(effectId).getProperty(name);
    }

    @Override
    public void dispose() {
        for (GraphParticleEffect value : particleEffects.values()) {
            value.dispose();
        }
    }
}
