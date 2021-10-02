package com.gempukku.libgdx.graph.plugin.models.producer.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelImpl;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public interface ModelRenderingStrategy {
    void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback);

    interface StrategyCallback {
        void begin();

        void process(GraphModelImpl graphModel, String tag);

        void end();
    }
}
