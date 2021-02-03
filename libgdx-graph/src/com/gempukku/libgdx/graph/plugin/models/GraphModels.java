package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.gempukku.libgdx.graph.shader.TransformUpdate;

/**
 * Main interface that is used to operate on models rendered by GraphShaders. Any operation done on the models should
 * be done via use of this interface.
 */
public interface GraphModels {
    /**
     * Registers the specified model with Graph Shaders.
     *
     * @param model Model to register
     * @return Returns model that is used further on, when referring to the model.
     */
    GraphModel registerModel(Model model);

    /**
     * Removes the specified model from Graph Shaders, freeing up the memory.
     * This method also removes all model instances created from that model.
     *
     * @param model model that was returned from the registerModel call.
     */
    void removeModel(GraphModel model);

    /**
     * Adds tag that will be added to every model instance that is subsequently created from the model.
     * Rendering with shaders with this tag will not be optimized by the engine.
     *
     * @param model model that was returned from the registerModel call.
     * @param tag   Tag that should be added to each subsequently created model instance.
     */
    void addModelDefaultTag(GraphModel model, String tag);

    /**
     * Adds tag that will be added to every model instance that is subsequently created from the model.
     * Note, that adding the same tag multiple times with different TagPerformanceHint will result in using only the
     * last setting.
     *
     * @param model               model that was returned from the registerModel call.
     * @param tag                 Tag that should be added to each subsequently created model instance.
     * @param tagOptimizationHint Specifies, how this tag will be used with model instances subsequently created.
     * @deprecated Not yet finalized - do not use
     */
    @Deprecated
    void addModelDefaultTag(GraphModel model, String tag, TagOptimizationHint tagOptimizationHint);

    /**
     * Removes tag from list of tags that would be added to every model instance that is subsequently created from
     * the model. Please note - this DOES NOT remove the tag from model instances already created.
     *
     * @param model model that was returned from the registerModel call.
     * @param tag     Tag that should be removed.
     */
    void removeModelDefaultTag(GraphModel model, String tag);

    /**
     * Creates model instance based on the specified model. This model instance will not be optimized by the engine.
     *
     * @param model model that was returned from the registerModel call.
     * @return Returns modelInstance that is used further on, when referring to the model instance.
     */
    GraphModelInstance createModelInstance(GraphModel model);

    /**
     * Creates model instance based on the specified model. Provides hints to the engine, on how this model will
     * be used, so that engine can optimize the rendering having those in mind.
     *
     * @param model                        model that was returned from the registerModel call.
     * @param modelInstanceOptimizationHints Provides the engine some hints on how this model will be utilized.
     * @return Returns modelInstance that is used further on, when referring to the model instance.
     * @deprecated Not yet finalized - do not use
     */
    @Deprecated
    GraphModelInstance createModelInstance(GraphModel model, ModelInstanceOptimizationHints modelInstanceOptimizationHints);

    /**
     * Destroys and frees up any resources used by the specified model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     */
    void destroyModelInstance(GraphModelInstance modelInstance);

    /**
     * Updates transform of the given model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param transformUpdate An update operation that is performed on the transform of the model instance. Please note
     *                        - that the Matrix4 passed is NOT the actual instance of Matrix4 used by the model
     *                        internally.
     */
    void updateTransform(GraphModelInstance modelInstance, TransformUpdate transformUpdate);

    /**
     * Creates animation controller for the specified model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @return AnimationController used to animate the model instance.
     */
    AnimationController createAnimationController(GraphModelInstance modelInstance);

    /**
     * Adds tag to the model instance. These tags are used to specify, which shaders should render this model instance.
     * Rendering with shaders with this tag will not be optimized by the engine.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param tag             Tag to add.
     */
    void addTag(GraphModelInstance modelInstance, String tag);

    /**
     * Adds tag to the model instance. These tags are used to specify, which shaders should render this model instance.
     * Note, that adding the same tag multiple times with different TagPerformanceHint will result in using only the
     * last setting, and adding a tag with different hint than is currently in use by the model instance, effectively
     * means that the "cost" of removal of the tag has to be "paid", based on the previous setting.
     *
     * @param modelInstance     modelInstance that was returned from the createModelInstance call.
     * @param tag                 Tag to add.
     * @param tagOptimizationHint Specifies, how this tag will be used with this model instance.
     */
    void addTag(GraphModelInstance modelInstance, String tag, TagOptimizationHint tagOptimizationHint);

    /**
     * Removes tag from the model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param tag             Tag to remove.
     */
    void removeTag(GraphModelInstance modelInstance, String tag);

    /**
     * Removes all tags from the model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     */
    void removeAllTags(GraphModelInstance modelInstance);

    /**
     * Checks whether the model instance has the specified tag.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param tag             Tag to check the model instance for.
     * @return If the model instance has the tag.
     */
    boolean hasTag(GraphModelInstance modelInstance, String tag);

    /**
     * Sets property on the given model instance.
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param name            Name of the property.
     * @param value           Value of the property.
     */
    void setProperty(GraphModelInstance modelInstance, String name, Object value);

    /**
     * Un-sets the property from the given model instance. If the property is un-set, the default value for that
     * property will be used (specified in the Graph editor).
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param name            Name of the property.
     */
    void unsetProperty(GraphModelInstance modelInstance, String name);

    /**
     * Returns the value of a property from the given model instance.
     * Please note - that if the property is not set on the model instance, a null is returned and NOT the default
     * value (from Graph editor).
     *
     * @param modelInstance modelInstance that was returned from the createModelInstance call.
     * @param name            Name of the property.
     * @return Value of the property.
     */
    Object getProperty(GraphModelInstance modelInstance, String name);
}
