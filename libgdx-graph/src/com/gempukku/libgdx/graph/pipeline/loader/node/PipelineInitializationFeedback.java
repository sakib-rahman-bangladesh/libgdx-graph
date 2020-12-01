package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.shader.models.impl.PropertyContainerImpl;

public interface PipelineInitializationFeedback {
    void registerScreenShader(String tag, PropertyContainerImpl propertyContainer);
}
