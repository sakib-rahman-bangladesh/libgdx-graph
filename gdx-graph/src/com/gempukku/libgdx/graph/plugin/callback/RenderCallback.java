package com.gempukku.libgdx.graph.plugin.callback;

import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public interface RenderCallback {
    void renderCallback(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements);
}
