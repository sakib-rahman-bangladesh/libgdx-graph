package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public interface CustomRenderCallback {
    void renderCallback(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements);
}
