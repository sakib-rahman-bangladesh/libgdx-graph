package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class RenderOutputs {
    public static final RenderOutput drawToScreen = new DrawToScreen();

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
        public void output(RenderPipeline renderPipeline) {
            renderPipeline.drawTexture(renderPipeline.getDefaultBuffer(), null);

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
        public void output(RenderPipeline renderPipeline) {
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
