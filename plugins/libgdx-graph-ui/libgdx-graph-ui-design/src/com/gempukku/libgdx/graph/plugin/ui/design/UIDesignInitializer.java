package com.gempukku.libgdx.graph.plugin.ui.design;

import com.gempukku.libgdx.graph.plugin.ui.UIRuntimeInitializer;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignInitializer;

public class UIDesignInitializer implements PluginDesignInitializer {
    @Override
    public void initialize() {
        UIRuntimeInitializer.register();

        UIPipelineConfiguration.register(new UIRendererBoxProducer());
    }
}
