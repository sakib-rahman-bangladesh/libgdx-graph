package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.plugin.models.TagOptimizationHint;

public interface IGraphModel extends GraphModel, Disposable {
    void addDefaultTag(String tag, TagOptimizationHint tagOptimizationHint);

    ObjectMap.Entries<String, TagOptimizationHint> getDefaultTags();

    void removeDefaultTag(String tag);

    IGraphModelInstance createInstance(ModelInstanceOptimizationHints modelInstanceOptimizationHints);
}
