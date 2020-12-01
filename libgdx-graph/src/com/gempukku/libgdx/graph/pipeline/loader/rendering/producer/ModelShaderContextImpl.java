package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.gempukku.libgdx.graph.shader.ModelShaderContext;

public class ModelShaderContextImpl extends ShaderContextImpl implements ModelShaderContext {
    private Renderable renderable;

    public void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }

    @Override
    public Renderable getRenderable() {
        return renderable;
    }
}
