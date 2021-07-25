package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.plugin.maps.producer.MapsRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class MapsRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public MapsRendererBoxProducer() {
        super(new MapsRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(id);

        StringBoxPart<PipelineFieldType> mapId = new StringBoxPart<>("Map id: ", "id");
        if (data != null)
            mapId.initialize(data);
        result.addGraphBoxPart(mapId);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
