package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public class RenderOutputs {
    public static final RenderOutput drawToScreen = new DrawToScreenImpl();

    public static class DrawToScreen implements RenderOutput {
        private int x;
        private int y;
        private int width;
        private int height;

        public DrawToScreen(int x, int y, int width, int height) {
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

    public class DrawToScreenScaled implements RenderOutput {
        private Scaling scaling;
        private int align;
        private int width;
        private int height;

        public DrawToScreenScaled(Scaling scaling, int align, int width, int height) {
            this.scaling = scaling;
            this.align = align;
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
            RenderPipelineBuffer defaultBuffer = renderPipeline.getDefaultBuffer();

            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();

            Vector2 size = scaling.apply(defaultBuffer.getWidth(), defaultBuffer.getHeight(), screenWidth, screenHeight);

            float spaceX = Align.isLeft(align) ? 0 : (Align.isRight(align) ? (screenWidth - size.x) : ((screenWidth - size.x) / 2));
            float spaceY = Align.isTop(align) ? 0 : (Align.isBottom(align) ? (screenHeight - size.y) : ((screenHeight - size.y) / 2));

            renderPipeline.drawTexture(defaultBuffer, null, pipelineRenderingContext,
                    spaceX, spaceY, size.x, size.y);

            renderPipeline.returnFrameBuffer(defaultBuffer);
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

            // Setup a buffer to draw on, with replaced texture
            RenderPipelineBufferImpl tmpBuffer = (RenderPipelineBufferImpl) renderPipeline.getNewFrameBuffer(currentBuffer);
            Texture oldTexture = tmpBuffer.getColorBufferTexture();
            tmpBuffer.getColorBuffer().setColorTexture(texture);

            renderPipeline.drawTexture(currentBuffer, tmpBuffer, pipelineRenderingContext);

            // Return buffer to previous state and give it back to pipeline
            tmpBuffer.getColorBuffer().setColorTexture(oldTexture);
            renderPipeline.returnFrameBuffer(tmpBuffer);

            // Return the default buffer
            renderPipeline.returnFrameBuffer(currentBuffer);
        }
    }

    private static class DrawToScreenImpl implements RenderOutput {
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
}
