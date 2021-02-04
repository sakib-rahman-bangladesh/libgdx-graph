package com.gempukku.libgdx.graph.plugin.maps.design;

import com.gempukku.libgdx.graph.plugin.maps.MapsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsLayerIdsRendererBoxProducer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsLayersRendererBoxProducer;
import com.gempukku.libgdx.graph.plugin.maps.design.producer.MapsRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class MapsPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        MapsPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new MapsRendererBoxProducer());
        UIPipelineConfiguration.register(new MapsLayersRendererBoxProducer());
        UIPipelineConfiguration.register(new MapsLayerIdsRendererBoxProducer());
    }
}
