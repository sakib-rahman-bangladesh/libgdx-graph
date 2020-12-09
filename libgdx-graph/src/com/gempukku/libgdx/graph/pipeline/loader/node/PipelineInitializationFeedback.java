package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public interface PipelineInitializationFeedback {
    void registerScreenShader(String tag, PropertyContainerImpl propertyContainer);

    void registerParticleEffect(String tag, int maxNumberOfParticles, int initialParticles, float particlesPerSecond);

    void registerModelAttribute(VertexAttribute vertexAttribute);
}
