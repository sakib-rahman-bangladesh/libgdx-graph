package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class ShaderBackToFrontModelRenderingStrategy implements ModelRenderingStrategy {
    private Array<GraphModel> orderingArray = new Array<>();
    private DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Back_To_Front);

    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            orderingArray.clear();
            for (GraphModel model : models.getModels(tag))
                if (model.getRenderableModel().isRendered(camera))
                    orderingArray.add(model);
            modelSorter.sort(camera.position, orderingArray);
            for (GraphModel graphModel : orderingArray) {
                callback.process(graphModel, tag);
            }
        }
        callback.end();
    }
}
