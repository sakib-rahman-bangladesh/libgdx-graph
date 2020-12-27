package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.model.GraphModel;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;

public interface IGraphModel extends GraphModel, Disposable {
    void addDefaultTag(String tag, TagOptimizationHint tagOptimizationHint);

    ObjectMap.Entries<String, TagOptimizationHint> getDefaultTags();

    void removeDefaultTag(String tag);

    IGraphModelInstance createInstance(ModelInstanceOptimizationHints modelInstanceOptimizationHints);
}
