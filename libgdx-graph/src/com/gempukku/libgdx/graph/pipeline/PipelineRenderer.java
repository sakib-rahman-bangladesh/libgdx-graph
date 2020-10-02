package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.TimeKeeper;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

public interface PipelineRenderer extends PipelinePropertySource, Disposable {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    GraphShaderModels getGraphShaderModels();

    void setTimeKeeper(TimeKeeper timeKeeper);

    void render(float delta, RenderOutput renderOutput);
}
