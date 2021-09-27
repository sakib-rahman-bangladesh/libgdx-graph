package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBufferImpl;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBuffer;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public class RenderPipelineImpl implements RenderPipeline {
    private final BufferCopyHelper bufferCopyHelper;
    private final TextureFrameBufferCache textureFrameBufferCache;
    private final RenderPipelineBufferImpl defaultBuffer = new RenderPipelineBufferImpl();

    public RenderPipelineImpl(BufferCopyHelper bufferCopyHelper, TextureFrameBufferCache textureFrameBufferCache) {
        this.bufferCopyHelper = bufferCopyHelper;
        this.textureFrameBufferCache = textureFrameBufferCache;
    }

    @Override
    public RenderPipelineBufferImpl initializeDefaultBuffer(int width, int height, Pixmap.Format format) {
        defaultBuffer.setColorBuffer(textureFrameBufferCache.getOrCreateFrameBuffer(width, height, format));
        return defaultBuffer;
    }

    @Override
    public void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) renderPipelineBuffer;
        if (buffer.getDepthBuffer() == null) {
            TextureFrameBuffer depthBuffer = textureFrameBufferCache.getOrCreateFrameBuffer(
                    buffer.getWidth(), buffer.getHeight(),
                    renderPipelineBuffer.getColorBufferTexture().getTextureData().getFormat());
            depthBuffer.begin();
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            depthBuffer.end();
            buffer.setDepthBuffer(depthBuffer);
        }
    }

    @Override
    public RenderPipelineBufferImpl getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom) {
        RenderPipelineBufferImpl source = (RenderPipelineBufferImpl) takeSettingsFrom;
        return getNewFrameBuffer(source.getWidth(), source.getHeight(),
                takeSettingsFrom.getColorBufferTexture().getTextureData().getFormat());
    }

    @Override
    public RenderPipelineBufferImpl getNewFrameBuffer(int width, int height, Pixmap.Format format) {
        TextureFrameBuffer frameBuffer = textureFrameBufferCache.getOrCreateFrameBuffer(width, height, format);
        RenderPipelineBufferImpl renderPipelineBuffer = new RenderPipelineBufferImpl();
        renderPipelineBuffer.setColorBuffer(frameBuffer);
        return renderPipelineBuffer;
    }

    @Override
    public void returnFrameBuffer(RenderPipelineBuffer frameBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) frameBuffer;
        textureFrameBufferCache.returnBuffer(buffer.getColorBuffer());
        TextureFrameBuffer depthBuffer = buffer.getDepthBuffer();
        if (depthBuffer != null) {
            textureFrameBufferCache.returnBuffer(depthBuffer);
            buffer.setDepthBuffer(null);
        }
    }

    @Override
    public void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext) {
        RenderPipelineBufferImpl fromBuffer = (RenderPipelineBufferImpl) paint;
        RenderPipelineBufferImpl toBuffer = (RenderPipelineBufferImpl) canvas;
        bufferCopyHelper.copy(fromBuffer.getColorBuffer(), toBuffer != null ? toBuffer.getColorBuffer() : null, pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());
    }

    @Override
    public void drawTexture(RenderPipelineBuffer paint, RenderPipelineBuffer canvas, PipelineRenderingContext pipelineRenderingContext, float x, float y, float width, float height) {
        RenderPipelineBufferImpl fromBuffer = (RenderPipelineBufferImpl) paint;
        RenderPipelineBufferImpl toBuffer = (RenderPipelineBufferImpl) canvas;
        bufferCopyHelper.copy(fromBuffer.getColorBuffer(), toBuffer != null ? toBuffer.getColorBuffer() : null,
                pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender(), x, y, width, height);
    }

    @Override
    public void swapColorTextures(RenderPipelineBuffer buffer1, RenderPipelineBuffer buffer2) {
        RenderPipelineBufferImpl first = (RenderPipelineBufferImpl) buffer1;
        RenderPipelineBufferImpl second = (RenderPipelineBufferImpl) buffer2;
        Texture oldTexture = first.getColorBuffer().setColorTexture(second.getColorBufferTexture());
        second.getColorBuffer().setColorTexture(oldTexture);
    }

    public RenderPipelineBuffer getDefaultBuffer() {
        return defaultBuffer;
    }
}
