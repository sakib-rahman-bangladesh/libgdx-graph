package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.GraphLights;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;

public class ModelShaderRendererPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ModelShaderRendererPipelineNodeConfiguration() {
        super("GraphShaderRenderer", "Model Shaders", "Shaders");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("lights", "Graph Lights", GraphLights));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
