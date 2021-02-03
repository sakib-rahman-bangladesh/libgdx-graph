package com.gempukku.libgdx.graph.pipeline.loader;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    <T> T getPrivatePluginData(Class<T> clazz);

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();

    RenderContext getRenderContext();

    FullScreenRender getFullScreenRender();
}
