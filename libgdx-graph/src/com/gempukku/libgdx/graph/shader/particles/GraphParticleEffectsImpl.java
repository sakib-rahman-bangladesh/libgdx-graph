package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.utils.ObjectMap;

public class GraphParticleEffectsImpl implements GraphParticleEffects {
    private ObjectMap<String, GraphParticleEffect> particleEffects = new ObjectMap<>();

    @Override
    public String createEffect(String tag, ParticleGenerator particleGenerator) {
        return null;
    }

    @Override
    public void startEffect(String effectId) {

    }

    @Override
    public void updateParticles(String effectId, ParticleUpdater particleUpdater) {

    }

    @Override
    public void stopEffect(String effectId) {

    }

    @Override
    public void destroyEffect(String effectId) {

    }

    @Override
    public void setProperty(String effectId, String name, Object value) {

    }

    @Override
    public void unsetProperty(String effectId, String name) {

    }

    @Override
    public Object getProperty(String effectId, String name) {
        return null;
    }
}
