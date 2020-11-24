package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline, RenderContext renderContext);
}
