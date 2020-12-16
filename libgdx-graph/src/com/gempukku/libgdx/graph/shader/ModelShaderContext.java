package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelInstanceData;

public interface ModelShaderContext extends ShaderContext {
    ModelInstanceData getModelInstanceData();
}
