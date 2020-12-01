package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public interface ShaderContext {
    int getRenderWidth();

    int getRenderHeight();

    Texture getDepthTexture();

    Texture getColorTexture();

    Camera getCamera();

    GraphShaderEnvironment getGraphShaderEnvironment();

    TimeProvider getTimeProvider();

    Object getProperty(String name);
}
