package com.gempukku.libgdx.graph.plugin.models.producer.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelImpl;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class FrontToBackRenderingStrategy implements ModelRenderingStrategy {
    private Array<GraphModelImpl> orderingArray = new Array<>();
    private DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Front_To_Back);

    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, ModelRenderingStrategy.StrategyCallback callback) {
        callback.begin();
        orderingArray.clear();
        for (String tag : tags) {
            for (GraphModelImpl model : models.getModels(tag))
                if (model.getRenderableModel().isRendered(camera))
                    orderingArray.add(model);
        }
        modelSorter.sort(camera.position, orderingArray);
        for (GraphModelImpl graphModel : orderingArray) {
            callback.process(graphModel, graphModel.getTag());
        }
        callback.end();
    }
}
