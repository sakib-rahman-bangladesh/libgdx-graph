package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBufferImpl;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBuffer;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;

import java.util.Iterator;

public class RenderPipelineImpl implements RenderPipeline {
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();
    private RenderPipelineBufferImpl defaultBuffer;

    private Array<TextureFrameBuffer> oldFrameBuffers = new Array<>();
    private Array<TextureFrameBuffer> newFrameBuffers = new Array<>();

    public void startFrame() {
    }

    public void endFrame() {
        defaultBuffer = null;
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        oldFrameBuffers.addAll(newFrameBuffers);
        newFrameBuffers.clear();
    }

    public void cleanup() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        for (FrameBuffer freeFrameBuffer : newFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        newFrameBuffers.clear();
        bufferCopyHelper.dispose();
    }

    @Override
    public RenderPipelineBufferImpl initializeDefaultBuffer(int width, int height, Pixmap.Format format) {
        if (defaultBuffer != null)
            throw new IllegalStateException("Default buffer already initialized");
        return defaultBuffer = getNewFrameBuffer(width, height, format);
    }

    @Override
    public void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) renderPipelineBuffer;
        if (buffer.getDepthBuffer() == null) {
            TextureFrameBuffer depthBuffer = getOrCreateFrameBuffer(
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
        TextureFrameBuffer frameBuffer = getOrCreateFrameBuffer(width, height, format);
        return new RenderPipelineBufferImpl(frameBuffer);
    }

    private TextureFrameBuffer getOrCreateFrameBuffer(int width, int height, Pixmap.Format format) {
        TextureFrameBuffer buffer = extractFrameBuffer(width, height, this.newFrameBuffers);
        if (buffer != null)
            return buffer;
        buffer = extractFrameBuffer(width, height, this.oldFrameBuffers);
        if (buffer != null)
            return buffer;

        return new TextureFrameBuffer(width, height, format);
    }

    private TextureFrameBuffer extractFrameBuffer(int width, int height, Array<TextureFrameBuffer> frameBuffers) {
        Iterator<TextureFrameBuffer> iterator = frameBuffers.iterator();
        while (iterator.hasNext()) {
            TextureFrameBuffer buffer = iterator.next();
            if (buffer.getWidth() == width && buffer.getHeight() == height) {
                iterator.remove();
                return buffer;
            }
        }
        return null;
    }

    @Override
    public void returnFrameBuffer(RenderPipelineBuffer frameBuffer) {
        RenderPipelineBufferImpl buffer = (RenderPipelineBufferImpl) frameBuffer;
        newFrameBuffers.add(buffer.getColorBuffer());
        TextureFrameBuffer depthBuffer = buffer.getDepthBuffer();
        if (depthBuffer != null)
            newFrameBuffers.add(depthBuffer);
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

    public void dispose() {
        cleanup();
    }
}
