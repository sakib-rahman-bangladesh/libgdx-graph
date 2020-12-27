package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.GraphModel;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;

public interface IGraphModel extends GraphModel, Disposable {
    void addDefaultTag(String tag, TagOptimizationHint tagOptimizationHint);

    void removeDefaultTag(String tag);

    IGraphModelInstance createInstance(ModelInstanceOptimizationHints modelInstanceOptimizationHints);
}
