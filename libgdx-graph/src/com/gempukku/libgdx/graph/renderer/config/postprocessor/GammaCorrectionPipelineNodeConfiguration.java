package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class GammaCorrectionPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection");
        addNodeInput(
                new PipelineNodeInputImpl(false, "gamma",
                        Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(true, "input",
                        Predicates.equalTo(PropertyType.RenderPipeline)));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }
}
