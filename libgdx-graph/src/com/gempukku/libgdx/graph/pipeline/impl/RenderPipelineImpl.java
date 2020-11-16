package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;

import java.util.Iterator;

public class RenderPipelineImpl implements RenderPipeline {
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();
    private RenderPipelineBuffer mainBuffer;

    private Array<FrameBuffer> oldFrameBuffers = new Array<FrameBuffer>();
    private Array<FrameBuffer> newFrameBuffers = new Array<FrameBuffer>();

    public void startFrame(int width, int height) {
    }

    public void endFrame() {
        mainBuffer = null;
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
    public void setCurrentBuffer(RenderPipelineBuffer frameBuffer) {
        mainBuffer = frameBuffer;
    }

    @Override
    public void enrichWithDepthBuffer(RenderPipelineBuffer renderPipelineBuffer) {
        if (renderPipelineBuffer.getDepthBuffer() == null) {
            FrameBuffer depthBuffer = getOrCreateFrameBuffer(
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

    private FrameBuffer getOrCreateFrameBuffer(int width, int height, Pixmap.Format format) {
        FrameBuffer buffer = extractFrameBuffer(width, height, this.newFrameBuffers);
        if (buffer != null)
            return buffer;
        buffer = extractFrameBuffer(width, height, this.oldFrameBuffers);
        if (buffer != null)
            return buffer;

        GLFrameBuffer.FrameBufferBuilder frameBufferBuilder = new GLFrameBuffer.FrameBufferBuilder(width, height);
        frameBufferBuilder.addBasicColorTextureAttachment(format);
        frameBufferBuilder.addBasicDepthRenderBuffer();
        return frameBufferBuilder.build();
    }

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    private FrameBuffer extractFrameBuffer(int width, int height, Array<FrameBuffer> frameBuffers) {
        Iterator<FrameBuffer> iterator = frameBuffers.iterator();
        while (iterator.hasNext()) {
            FrameBuffer buffer = iterator.next();
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
        FrameBuffer depthBuffer = frameBuffer.getDepthBuffer();
        if (depthBuffer != null)
            newFrameBuffers.add(depthBuffer);
    }

    public RenderPipelineBuffer getCurrentBuffer() {
        return mainBuffer;
    }

    public void dispose() {
        cleanup();
    }
}
