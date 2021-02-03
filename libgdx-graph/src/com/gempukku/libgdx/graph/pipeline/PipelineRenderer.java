package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.model.GraphModels;
import com.gempukku.libgdx.graph.shader.screen.GraphScreenShaders;

public interface PipelineRenderer extends PipelinePropertySource, Disposable {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    <T> T getPluginData(Class<T> clazz);

    GraphModels getGraphShaderModels();

    GraphScreenShaders getGraphScreenShaders();

    void render(RenderOutput renderOutput);
}
