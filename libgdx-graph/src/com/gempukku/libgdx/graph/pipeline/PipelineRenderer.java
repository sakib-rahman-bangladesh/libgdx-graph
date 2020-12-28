package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.GraphModels;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.shader.screen.GraphScreenShaders;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.time.TimeKeeper;

public interface PipelineRenderer extends PipelinePropertySource, Disposable {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    GraphModels getGraphShaderModels();

    GraphSprites getGraphSprites();

    GraphScreenShaders getGraphScreenShaders();

    GraphParticleEffects getGraphParticleEffects();

    void setTimeKeeper(TimeKeeper timeKeeper);

    float getTime();

    void render(float delta, RenderOutput renderOutput);
}
