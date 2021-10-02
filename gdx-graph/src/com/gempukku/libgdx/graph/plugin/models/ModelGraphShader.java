package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderContextImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ModelGraphShader extends GraphShader {
    public ModelGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public void render(ModelShaderContextImpl shaderContext, GraphModelImpl graphModel) {
        RenderableModel renderableModel = graphModel.getRenderableModel();

        shaderContext.setLocalPropertyContainer(renderableModel.getPropertyContainer());
        shaderContext.setRenderableModel(renderableModel);

        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        renderableModel.render(shaderContext.getCamera(), this);
    }
}
