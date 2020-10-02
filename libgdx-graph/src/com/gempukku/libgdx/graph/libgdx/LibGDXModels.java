package com.gempukku.libgdx.graph.libgdx;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;

public class LibGDXModels implements PipelineRendererModels {
    private ObjectSet<RenderableProvider> models = new ObjectSet<>();

    public void addRenderableProvider(RenderableProvider renderableProvider) {
        models.add(renderableProvider);
    }

    public void removeRenderableProvider(RenderableProvider renderableProvider) {
        models.remove(renderableProvider);
    }

    @Override
    public ObjectSet<RenderableProvider> getModels() {
        return models;
    }
}
