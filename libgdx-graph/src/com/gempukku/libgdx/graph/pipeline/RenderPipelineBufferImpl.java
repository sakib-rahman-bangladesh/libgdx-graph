package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.Texture;

public class RenderPipelineBufferImpl implements RenderPipelineBuffer {
    private TextureFrameBuffer colorBuffer;
    private TextureFrameBuffer depthBuffer;

    public TextureFrameBuffer getDepthBuffer() {
        return depthBuffer;
    }

    public void setDepthBuffer(TextureFrameBuffer depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    public Texture getDepthBufferTexture() {
        return depthBuffer.getColorBufferTexture();
    }

    public TextureFrameBuffer getColorBuffer() {
        return colorBuffer;
    }

    public void setColorBuffer(TextureFrameBuffer colorBuffer) {
        this.colorBuffer = colorBuffer;
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
