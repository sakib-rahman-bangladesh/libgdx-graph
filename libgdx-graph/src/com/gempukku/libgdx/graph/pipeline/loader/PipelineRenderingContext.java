package com.gempukku.libgdx.graph.pipeline.loader;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.shader.model.impl.GraphShaderModelsImpl;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffectsImpl;
import com.gempukku.libgdx.graph.shader.screen.ScreenShadersImpl;
import com.gempukku.libgdx.graph.shader.sprite.impl.GraphSpritesImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    GraphShaderModelsImpl getGraphShaderModels();

    GraphSpritesImpl getGraphSprites();

    ScreenShadersImpl getScreenShaders();

    GraphParticleEffectsImpl getGraphParticleEffects();

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();

    RenderContext getRenderContext();

    FullScreenRender getFullScreenRender();
}
