package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public interface CustomRenderCallback {
    void renderCallback(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements);
}
