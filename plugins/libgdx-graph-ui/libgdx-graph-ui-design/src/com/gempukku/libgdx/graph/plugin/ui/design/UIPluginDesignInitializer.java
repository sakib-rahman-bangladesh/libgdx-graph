package com.gempukku.libgdx.graph.plugin.ui.design;

import com.gempukku.libgdx.graph.plugin.ui.UIPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class UIPluginDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        UIPluginRuntimeInitializer.register();

        UIPipelineConfiguration.register(new UIRendererBoxProducer());
    }
}
