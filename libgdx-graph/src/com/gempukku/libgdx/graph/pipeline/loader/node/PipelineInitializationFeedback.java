package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.shader.models.impl.PropertyContainerImpl;

public interface PipelineInitializationFeedback {
    void registerScreenShader(String tag, PropertyContainerImpl propertyContainer);

    void registerModelAttribute(VertexAttribute vertexAttribute);
}
