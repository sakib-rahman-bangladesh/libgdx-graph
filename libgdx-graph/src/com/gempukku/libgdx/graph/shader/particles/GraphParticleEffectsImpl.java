package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffectsImpl implements GraphParticleEffects, Disposable {
    private ObjectSet<GraphParticleEffectImpl> particleEffects = new ObjectSet<>();
    private ObjectMap<String, ParticleEffectConfiguration> effectsConfiguration = new ObjectMap<>();
    private TimeProvider timeProvider;

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void registerEffect(String tag, ParticlesGraphShader shader) {
        if (effectsConfiguration.containsKey(tag))
            throw new IllegalStateException("Duplicate particle effect with tag - " + tag);
        effectsConfiguration.put(tag, new ParticleEffectConfiguration(shader.getVertexAttributes(), shader.getMaxNumberOfParticles(), shader.getInitialParticles(), 1f / shader.getPerSecondParticles()));
    }

    public Iterable<GraphParticleEffectImpl> getParticleEffects() {
        return particleEffects;
    }

    @Override
    public GraphParticleEffect createEffect(String tag, ParticleGenerator particleGenerator) {
        return createEffect(tag, particleGenerator, null);
    }

    @Override
    public <T> GraphParticleEffect createEffect(String tag, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass) {
        ParticleEffectConfiguration configuration = effectsConfiguration.get(tag);
        if (configuration == null)
            throw new IllegalArgumentException("Unable to find particle effect with tag - " + tag);

        GraphParticleEffectImpl particleEffect = new GraphParticleEffectImpl(tag, configuration, particleGenerator, particleDataClass != null);
        particleEffects.add(particleEffect);
        return particleEffect;
    }

    @Override
    public void setGenerator(GraphParticleEffect effect, ParticleGenerator particleGenerator) {
        setGenerator(effect, particleGenerator, null);
    }

    @Override
    public <T> void setGenerator(GraphParticleEffect effect, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass) {
        getEffect(effect).setParticleGenerator(particleGenerator);
    }

    @Override
    public void startEffect(GraphParticleEffect effect) {
        getEffect(effect).start();
    }

    @Override
    public void updateParticles(GraphParticleEffect effect, ParticleUpdater particleUpdater) {
        updateParticles(effect, particleUpdater, null);
    }

    @Override
    public <T> void updateParticles(GraphParticleEffect effect, ParticleUpdater<T> particleUpdater, Class<T> particleDataClass) {
        getEffect(effect).update(timeProvider, particleUpdater, particleDataClass != null);
    }

    @Override
    public void stopEffect(GraphParticleEffect effect) {
        getEffect(effect).stop();
    }

    @Override
    public void destroyEffect(GraphParticleEffect effect) {
        GraphParticleEffectImpl effectImpl = getEffect(effect);
        particleEffects.remove(effectImpl);
        effectImpl.dispose();
    }

    @Override
    public void setProperty(GraphParticleEffect effect, String name, Object value) {
        getEffect(effect).setProperty(name, value);
    }

    @Override
    public void unsetProperty(GraphParticleEffect effect, String name) {
        getEffect(effect).unsetProperty(name);
    }

    @Override
    public Object getProperty(GraphParticleEffect effect, String name) {
        return getEffect(effect).getProperty(name);
    }

    @Override
    public void dispose() {
        for (GraphParticleEffectImpl value : particleEffects) {
            value.dispose();
        }
    }

    private GraphParticleEffectImpl getEffect(GraphParticleEffect effect) {
        GraphParticleEffectImpl effectImpl = (GraphParticleEffectImpl) effect;
        if (!particleEffects.contains(effectImpl))
            throw new IllegalArgumentException("Unable to find the graph particle effect");
        return effectImpl;
    }
}
