package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public interface PipelineInitializationFeedback {
    void registerScreenShader(String tag, PropertyContainerImpl propertyContainer);

    void registerParticleEffect(String tag, int maxNumberOfParticles, int initialParticles, float particlesPerSecond);

    void registerModelAttribute(String tag, Array<VertexAttribute> vertexAttributes);

    void registerSpriteShader(String tag, Array<VertexAttribute> vertexAttributes, ObjectMap<String, PropertySource> shaderProperties);
}
