package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;

public interface GraphModel extends Disposable {
    void addDefaultTag(String tag, TagOptimizationHint tagOptimizationHint);

    void removeDefaultTag(String tag);

    GraphModelInstance createInstance(String instanceId, ModelInstanceOptimizationHints modelInstanceOptimizationHints);
}
