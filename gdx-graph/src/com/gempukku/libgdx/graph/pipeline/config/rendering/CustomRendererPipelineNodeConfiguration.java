package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class CustomRendererPipelineNodeConfiguration extends NodeConfigurationImpl {
    public CustomRendererPipelineNodeConfiguration() {
        super("CustomRenderer", "Custom renderer", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, "Boolean"));
        addNodeInput(
                new GraphNodeInputImpl("callback", "Callback", true, PipelineFieldType.Callback));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
