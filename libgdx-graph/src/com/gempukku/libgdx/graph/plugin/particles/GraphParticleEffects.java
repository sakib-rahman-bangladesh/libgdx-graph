package com.gempukku.libgdx.graph.plugin.particles;

import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;

/**
 * Main interface that is used to work with particle effects. Any operation done on the particle effects should
 * be done through this interface.
 */
public interface GraphParticleEffects {
    /**
     * Creates a new particle effect with the given tag (as defined in the pipeline).
     * The particles generated by this effect will be generated using the provided particle generator.
     * These particles will NOT contain space for additional data for each particle.
     *
     * @param tag               Tag identifying the particle effect to generate.
     * @param particleGenerator Particle generator used to generate particles.
     * @return Object identifying the particle effect.
     */
    GraphParticleEffect createEffect(String tag, ParticleGenerator particleGenerator);

    /**
     * Creates a new particle effect with the given tag (as defined in the pipeline).
     * The particles generated by this effect will be generated using the provided particle generator.
     * These particles will contain space for additional data for each particle, with the class of type passed
     * as a parameter.
     *
     * @param tag               Tag identifying the particle effect to generate.
     * @param particleGenerator Particle generator used to generate particles.
     * @param particleDataClass Class used for storing per-particle data.
     * @param <T>               Class used for particle data
     * @return Object identifying the particle effect.
     */
    <T> GraphParticleEffect createEffect(String tag, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass);

    void setGenerator(GraphParticleEffect effect, ParticleGenerator particleGenerator);

    <T> void setGenerator(GraphParticleEffect effect, ParticleGenerator<T> particleGenerator, Class<T> particleDataClass);

    /**
     * Starts generation of particles for the identified effect.
     *
     * @param effect The effect object.
     */
    void startEffect(GraphParticleEffect effect);

    /**
     * Update all particles in the identified effect. This will not provide access to the per-particle data.
     *
     * @param effect          The effect object.
     * @param particleUpdater A class that is called to update each particle.
     */
    void updateParticles(GraphParticleEffect effect, ParticleUpdater particleUpdater);

    /**
     * Update all particles in the identified effect. This method allows access to the particle data.
     *
     * @param effect            The effect object.
     * @param particleUpdater   A class that is called to update each particle.
     * @param particleDataClass Class used for storing per-particle data.
     * @param <T>               Class used for particle data
     */
    <T> void updateParticles(GraphParticleEffect effect, ParticleUpdater<T> particleUpdater, Class<T> particleDataClass);

    /**
     * Stops generation of particles for the identified effect.
     *
     * @param effect The effect object.
     */
    void stopEffect(GraphParticleEffect effect);

    /**
     * Destroys the identified effect.
     *
     * @param effect The effect object.
     */
    void destroyEffect(GraphParticleEffect effect);

    /**
     * Sets property on the given particle effect.
     *
     * @param effect The effect object.
     * @param name   Name of the property.
     * @param value  Value of the property.
     */
    void setProperty(GraphParticleEffect effect, String name, Object value);

    /**
     * Un-sets the property from the given particle effect. If the property is un-set, the default value for that
     * property will be used (specified in the Graph editor).
     *
     * @param effect The effect object.
     * @param name   Name of the property.
     */
    void unsetProperty(GraphParticleEffect effect, String name);

    /**
     * Returns the value of a property from the given particle effect.
     * Please note - that if the property is not set on the model instance, a null is returned and NOT the default
     * value (from Graph editor).
     *
     * @param effect The effect object.
     * @param name   Name of the property.
     * @return Value of the property.
     */
    Object getProperty(GraphParticleEffect effect, String name);

    void setGlobalProperty(String tag, String name, Object value);

    void unsetGlobalProperty(String tag, String name);

    Object getGlobalProperty(String tag, String name);
}
