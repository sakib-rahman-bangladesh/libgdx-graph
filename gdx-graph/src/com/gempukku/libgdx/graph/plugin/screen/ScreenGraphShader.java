package com.gempukku.libgdx.graph.plugin.screen;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class ScreenGraphShader extends GraphShader {
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();

    public ScreenGraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
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

    public PropertyContainerImpl getPropertyContainer() {
        return propertyContainer;
    }
}
