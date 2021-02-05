package com.gempukku.libgdx.graph.pipeline.producer.node;

public interface PipelineInitializationFeedback {
    <T> T getPrivatePluginData(Class<T> clazz);
}
