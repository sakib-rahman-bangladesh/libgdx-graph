package com.gempukku.libgdx.graph.plugin.maps.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class MapsRendererPipelineNodeConfiguration extends NodeConfigurationImpl {
    public MapsRendererPipelineNodeConfiguration() {
        super("MapRendererAll", "Map renderer", "Maps");
        addNodeInput(
                new GraphNodeInputImpl("enabled", "Enabled", false, "Boolean"));
        addNodeInput(
                new GraphNodeInputImpl("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Output", true, RenderPipeline));
    }
}
