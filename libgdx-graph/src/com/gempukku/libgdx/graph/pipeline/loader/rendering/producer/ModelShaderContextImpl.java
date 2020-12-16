package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.gempukku.libgdx.graph.shader.ModelShaderContext;

public class ModelShaderContextImpl extends ShaderContextImpl implements ModelShaderContext {
    private ModelInstanceData modelInstanceData;

    @Override
    public ModelInstanceData getModelInstanceData() {
        return modelInstanceData;
    }

    public void setModelInstanceData(ModelInstanceData modelInstanceData) {
        this.modelInstanceData = modelInstanceData;
    }
}
