package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;

public interface GraphShaderModel extends Disposable {
    void addDefaultTag(String tag, TagOptimizationHint tagOptimizationHint);

    void removeDefaultTag(String tag);

    GraphShaderModelInstance createInstance(String instanceId, ModelInstanceOptimizationHints modelInstanceOptimizationHints);
}
