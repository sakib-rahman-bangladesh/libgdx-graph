package com.gempukku.libgdx.graph.pipeline.loader;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.shader.models.impl.GraphShaderModelsImpl;
import com.gempukku.libgdx.graph.shader.models.impl.ScreenShadersImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    GraphShaderModelsImpl getGraphShaderModels();

    ScreenShadersImpl getScreenShaders();

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();

    RenderContext getRenderContext();

    FullScreenRender getFullScreenRender();
}
