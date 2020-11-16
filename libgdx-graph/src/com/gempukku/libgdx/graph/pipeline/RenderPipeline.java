package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Pixmap;
import com.gempukku.libgdx.graph.pipeline.impl.BufferCopyHelper;

public interface RenderPipeline {
    RenderPipelineBuffer getCurrentBuffer();

    void setCurrentBuffer(RenderPipelineBuffer frameBuffer);

    void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer);

    RenderPipelineBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format);

    RenderPipelineBuffer getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom);

    void returnFrameBuffer(RenderPipelineBuffer frameBuffer);

    BufferCopyHelper getBufferCopyHelper();
}
