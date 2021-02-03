package com.gempukku.libgdx.graph.pipeline.loader.node;

public interface PipelineInitializationFeedback {
    <T> T getPrivatePluginData(Class<T> clazz);
}
