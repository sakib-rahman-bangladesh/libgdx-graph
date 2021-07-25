package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class GammaCorrectionPipelineNodeConfiguration extends NodeConfigurationImpl {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection", "Gamma correction", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, "Boolean"));
        addNodeInput(
                new GraphNodeInputImpl("gamma", "Gamma", "Float"));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, "RenderPipeline"));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, "RenderPipeline"));
    }
}
