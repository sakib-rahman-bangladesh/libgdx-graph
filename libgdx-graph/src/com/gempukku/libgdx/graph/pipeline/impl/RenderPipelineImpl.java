package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.TextureFrameBuffer;

import java.util.Iterator;

public class RenderPipelineImpl implements RenderPipeline {
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();
    private RenderPipelineBuffer defaultBuffer;

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
    public RenderPipelineBuffer initializeDefaultBuffer(int width, int height, Pixmap.Format format) {
        return defaultBuffer = getNewFrameBuffer(width, height, format);
    }

    @Override
    public void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer) {
        if (renderPipelineBuffer.getDepthBuffer() == null) {
            TextureFrameBuffer depthBuffer = getOrCreateFrameBuffer(
                    renderPipelineBuffer.getWidth(), renderPipelineBuffer.getHeight(),
                    renderPipelineBuffer.getColorBufferTexture().getTextureData().getFormat());
            depthBuffer.begin();
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            depthBuffer.end();
            renderPipelineBuffer.setDepthBuffer(depthBuffer);
        }
    }

    @Override
    public RenderPipelineBuffer getNewFrameBuffer(RenderPipelineBuffer takeSettingsFrom) {
        return getNewFrameBuffer(takeSettingsFrom.getWidth(), takeSettingsFrom.getHeight(),
                takeSettingsFrom.getColorBufferTexture().getTextureData().getFormat());
    }

    @Override
    public RenderPipelineBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format) {
        return new RenderPipelineBuffer(getOrCreateFrameBuffer(width, height, format));
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

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
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
        newFrameBuffers.add(frameBuffer.getColorBuffer());
        TextureFrameBuffer depthBuffer = frameBuffer.getDepthBuffer();
        if (depthBuffer != null)
            newFrameBuffers.add(depthBuffer);
    }

    public RenderPipelineBuffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public void dispose() {
        cleanup();
    }
}
