package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.plugin.models.GraphModelInstance;
import com.gempukku.libgdx.graph.plugin.models.TagOptimizationHint;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelDataProducer;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public interface IGraphModelInstance extends GraphModelInstance {
    void addTag(String tag, TagOptimizationHint tagOptimizationHint);

    Iterable<String> getAllTags();

    void removeTag(String tag);

    void removeAllTags();

    PropertyContainerImpl getPropertyContainer();

    Matrix4 getTransformMatrix();

    AnimationController createAnimationController();

    Iterable<ModelDataProducer> getModelInstanceData();
}
