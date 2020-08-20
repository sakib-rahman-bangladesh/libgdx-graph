package com.gempukku.libgdx.graph.pipeline;

public interface PipelineRenderer extends PipelinePropertySource {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    void render(float delta, RenderOutput renderOutput);

    void dispose();
}