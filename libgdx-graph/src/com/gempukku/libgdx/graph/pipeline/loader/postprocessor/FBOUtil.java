package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;

public class FBOUtil {
    public static void swapColorBufferTextures(RenderPipelineBuffer fbo1, RenderPipelineBuffer fbo2) {
        Texture oldTexture = fbo1.getColorBuffer().setColorTexture(fbo2.getColorBufferTexture());
        fbo2.getColorBuffer().setColorTexture(oldTexture);
    }
}
