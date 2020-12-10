package com.gempukku.libgdx.graph.shader.screen;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.loader.FullScreenRender;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;

public class ScreenGraphShader extends GraphShader {
    public ScreenGraphShader(Texture defaultTexture) {
        super(defaultTexture);
        setCulling(BasicShader.Culling.back);
        setDepthTesting(BasicShader.DepthTesting.disabled);
    }

    public void render(ShaderContext shaderContext, FullScreenRender fullScreenRender) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        fullScreenRender.renderFullScreen(program);
    }
}
