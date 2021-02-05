package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;

public interface SpriteData {
    void render(ShaderContextImpl shaderContext, ShaderProgram shaderProgram, int[] attributeLocations);
}
