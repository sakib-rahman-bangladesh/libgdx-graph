package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;

public interface SpriteData {
    void render(ShaderContextImpl shaderContext, ShaderProgram shaderProgram, int[] attributeLocations);
}
