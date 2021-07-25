package com.gempukku.libgdx.graph.plugin.models.design.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeNormalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class AttributeNormalBoxProducer extends GraphBoxProducerImpl {
    public AttributeNormalBoxProducer() {
        super(new AttributeNormalShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        SelectBoxPart coordinatesBox = new SelectBoxPart("Coordinates", "coordinates", "world", "object");
        coordinatesBox.initialize(data);
        result.addGraphBoxPart(coordinatesBox);
        return result;
    }
}