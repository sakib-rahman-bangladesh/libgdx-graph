package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class RenderPipelineBuffer {
    private FrameBuffer colorBuffer;
    private FrameBuffer depthBuffer;

    public RenderPipelineBuffer(FrameBuffer colorBuffer) {
        this.colorBuffer = colorBuffer;
    }

    public FrameBuffer getDepthBuffer() {
        return depthBuffer;
    }

    public void setDepthBuffer(FrameBuffer depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    public Texture getDepthBufferTexture() {
        return depthBuffer.getColorBufferTexture();
    }

    public FrameBuffer getColorBuffer() {
        return colorBuffer;
    }

    public Texture getColorBufferTexture() {
        return colorBuffer.getColorBufferTexture();
    }

    public void beginColor() {
        colorBuffer.begin();
    }

    public void endColor() {
        colorBuffer.end();
    }

    public void beginDepth() {
        depthBuffer.begin();
    }

    public void endDepth() {
        depthBuffer.end();
    }

    public int getWidth() {
        return colorBuffer.getWidth();
    }

    public int getHeight() {
        return colorBuffer.getHeight();
    }
}
