package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.plugin.maps.producer.MapsLayersRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MapsLayersRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public MapsLayersRendererBoxProducer() {
        super(new MapsLayersRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(id);

        StringBoxPart<PipelineFieldType> mapId = new StringBoxPart<>("Map id: ", "id");
        if (data != null)
            mapId.initialize(data);
        result.addGraphBoxPart(mapId);

        StringBoxPart<PipelineFieldType> layers = new StringBoxPart<>("Layers: ", "layers");
        if (data != null)
            layers.initialize(data);
        result.addGraphBoxPart(layers);

        VisLabel description = new VisLabel("Comma separated list of layer names");
        description.setColor(Color.GRAY);
        result.addGraphBoxPart(
                new GraphBoxPartImpl<PipelineFieldType>(description, null));

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
