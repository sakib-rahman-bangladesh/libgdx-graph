package com.gempukku.libgdx.graph.plugin.models.producer.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelImpl;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class ShaderUnorderedModelRenderingStrategy implements ModelRenderingStrategy {
    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            for (GraphModelImpl model : models.getModels(tag)) {
                if (model.getRenderableModel().isRendered(camera)) {
                    callback.process(model, tag);
                }
            }
        }
        callback.end();
    }
}
