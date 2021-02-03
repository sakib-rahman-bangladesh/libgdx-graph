package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.shader.model.ModelGraphShader;

public interface PipelineInitializationFeedback {
    <T> T getPrivatePluginData(Class<T> clazz);

    void registerModelShader(String tag, ModelGraphShader shader);
}
