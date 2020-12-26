package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface SpriteData {
    void render(ShaderProgram shaderProgram, int[] attributeLocations);
}
