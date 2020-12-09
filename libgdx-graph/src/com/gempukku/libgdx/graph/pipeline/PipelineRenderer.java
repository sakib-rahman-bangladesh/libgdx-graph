package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.GraphShaderModels;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.shader.screen.ScreenShaders;
import com.gempukku.libgdx.graph.time.TimeKeeper;

public interface PipelineRenderer extends PipelinePropertySource, Disposable {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    GraphShaderModels getGraphShaderModels();

    ScreenShaders getScreenShaders();

    GraphParticleEffects getGraphParticleEffects();

    void setTimeKeeper(TimeKeeper timeKeeper);

    void render(float delta, RenderOutput renderOutput);
}
