package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Pixmap;

public interface RenderPipeline {
    RenderPipelineBuffer initializeDefaultBuffer(int width, int height, Pixmap.Format format);

    RenderPipelineBuffer getDefaultBuffer();

    void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer);

    RenderPipelineBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format);

    RenderPipelineBuffer getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom);

    void returnFrameBuffer(RenderPipelineBuffer frameBuffer);

    void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas);

    void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, float x, float y, float width, float height);

    void swapColorTextures(RenderPipelineBuffer buffer1, RenderPipelineBuffer buffer2);
}
