package com.gempukku.libgdx.graph.plugin.models.producer;

import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderContext;

public class ModelShaderContextImpl extends ShaderContextImpl implements ModelShaderContext {
    private ModelInstanceData modelInstanceData;

    public ModelShaderContextImpl(PluginPrivateDataSource pluginPrivateDataSource) {
        super(pluginPrivateDataSource);
    }

    @Override
    public ModelInstanceData getModelInstanceData() {
        return modelInstanceData;
    }

    public void setModelInstanceData(ModelInstanceData modelInstanceData) {
        this.modelInstanceData = modelInstanceData;
    }
}
