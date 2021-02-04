package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.plugin.maps.producer.MapsLayerIdsRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class MapsLayerIdsRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public MapsLayerIdsRendererBoxProducer() {
        super(new MapsLayerIdsRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);

        StringBoxPart<PipelineFieldType> mapId = new StringBoxPart<>(skin, "Map id: ", "id");
        if (data != null)
            mapId.initialize(data);
        result.addGraphBoxPart(mapId);

        StringBoxPart<PipelineFieldType> layers = new StringBoxPart<>(skin, "Layers ids: ", "layers");
        if (data != null)
            layers.initialize(data);
        result.addGraphBoxPart(layers);

        Label description = new Label("Comma separated list of layer ids", skin);
        description.setColor(Color.GRAY);
        result.addGraphBoxPart(
                new GraphBoxPartImpl<PipelineFieldType>(description, null));

        addConfigurationInputsAndOutputs(skin, result);
        return result;
    }
}
