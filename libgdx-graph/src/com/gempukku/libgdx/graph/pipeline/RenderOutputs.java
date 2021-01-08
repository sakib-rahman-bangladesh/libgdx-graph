package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;

public class RenderOutputs {
    public static final RenderOutput drawToScreen = new DrawToScreen();

    private static class DrawToScreenSpecified implements RenderOutput {
        private int x;
        private int y;
        private int width;
        private int height;

        public DrawToScreenSpecified(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getRenderWidth() {
            return width;
        }

        @Override
        public int getRenderHeight() {
            return height;
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext) {
            renderPipeline.drawTexture(renderPipeline.getDefaultBuffer(), null, pipelineRenderingContext,
                    x, y, width, height);

            renderPipeline.returnFrameBuffer(renderPipeline.getDefaultBuffer());
        }
    }

    private static class DrawToScreen implements RenderOutput {
        @Override
        public int getRenderWidth() {
            return Gdx.graphics.getWidth();
        }

        @Override
        public int getRenderHeight() {
            return Gdx.graphics.getHeight();
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext) {
            renderPipeline.drawTexture(renderPipeline.getDefaultBuffer(), null, pipelineRenderingContext);

            renderPipeline.returnFrameBuffer(renderPipeline.getDefaultBuffer());
        }
    }

    public static class RenderToTexture implements RenderOutput {
        private Texture texture;

        public RenderToTexture(Texture texture) {
            this.texture = texture;
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        @Override
        public int getRenderWidth() {
            return texture.getWidth();
        }

        @Override
        public int getRenderHeight() {
            return texture.getHeight();
        }

        @Override
        public void output(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext) {
            RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
            TextureData textureData = currentBuffer.getColorBufferTexture().getTextureData();
            textureData.prepare();
            Pixmap pixmap = textureData.consumePixmap();
            texture.draw(pixmap, 0, 0);
            if (textureData.disposePixmap())
                pixmap.dispose();

            renderPipeline.returnFrameBuffer(currentBuffer);
        }
    }
}
