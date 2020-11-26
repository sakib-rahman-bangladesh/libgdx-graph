package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext);
}
