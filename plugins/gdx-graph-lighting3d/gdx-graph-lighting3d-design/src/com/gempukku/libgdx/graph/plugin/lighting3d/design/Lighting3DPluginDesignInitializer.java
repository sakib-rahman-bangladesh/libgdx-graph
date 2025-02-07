package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.producer.*;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.UICommonShaderConfiguration;

public class Lighting3DPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        Lighting3DPluginRuntimeInitializer.register();

        UICommonShaderConfiguration.register(new PhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new BlinnPhongLightingBoxProducer());
        UICommonShaderConfiguration.register(new GraphBoxProducerImpl(new ApplyNormalMapShaderNodeConfiguration()));
        UICommonShaderConfiguration.register(new AmbientLightBoxProducer());
        UICommonShaderConfiguration.register(new DirectionalLightBoxProducer());
        UICommonShaderConfiguration.register(new PointLightBoxProducer());
        UICommonShaderConfiguration.register(new SpotlightBoxProducer());

        UIPipelineConfiguration.register(new ShadowShaderRendererBoxProducer());
    }
}
