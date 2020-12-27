package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelDataProducer;
import com.gempukku.libgdx.graph.shader.model.GraphModelInstance;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public interface IGraphModelInstance extends GraphModelInstance {
    void addTag(String tag, TagOptimizationHint tagOptimizationHint);

    void removeTag(String tag);

    void removeAllTags();

    PropertyContainerImpl getPropertyContainer();

    Matrix4 getTransformMatrix();

    AnimationController createAnimationController();

    Iterable<ModelDataProducer> getModelInstanceData();
}
