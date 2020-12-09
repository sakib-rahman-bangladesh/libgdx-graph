package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.RandomIdGenerator;

public class GraphParticleEffectsImpl implements GraphParticleEffects, Disposable {
    private ObjectMap<String, GraphParticleEffect> particleEffects = new ObjectMap<>();
    private ObjectMap<String, ParticleEffectConfiguration> effectsConfiguration = new ObjectMap<>();
    private RandomIdGenerator randomIdGenerator = new RandomIdGenerator(16);

    public void registerEffect(String tag, int maxNumberOfParticles, int initialParticles, float particlesPerSecond) {
        if (effectsConfiguration.containsKey(tag))
            throw new IllegalStateException("Duplicate particle effect with tag - " + tag);
        effectsConfiguration.put(tag, new ParticleEffectConfiguration(maxNumberOfParticles, initialParticles, 1f / particlesPerSecond));
    }

    public ObjectMap.Values<GraphParticleEffect> getParticleEffects() {
        return particleEffects.values();
    }

    @Override
    public String createEffect(String tag, ParticleGenerator particleGenerator) {
        ParticleEffectConfiguration configuration = effectsConfiguration.get(tag);
        if (configuration == null)
            throw new IllegalArgumentException("Unable to find particle effect with tag - " + tag);

        String effectId = randomIdGenerator.generateId();
        GraphParticleEffect particleEffect = new GraphParticleEffect(tag, configuration, particleGenerator);
        particleEffects.put(effectId, particleEffect);

        return effectId;
    }

    @Override
    public void startEffect(String effectId) {
        particleEffects.get(effectId).start();
    }

    @Override
    public void updateParticles(String effectId, ParticleUpdater particleUpdater) {
        particleEffects.get(effectId).update(particleUpdater);
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
