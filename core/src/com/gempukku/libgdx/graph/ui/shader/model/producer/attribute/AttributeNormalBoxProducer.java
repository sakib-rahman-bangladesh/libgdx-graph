package com.gempukku.libgdx.graph.ui.shader.model.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.model.attribute.AttributeNormalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class AttributeNormalBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public AttributeNormalBoxProducer() {
        super(new AttributeNormalShaderNodeConfiguration());
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        SelectBoxPart<ShaderFieldType> coordinatesBox = new SelectBoxPart<>(skin, "Coordinates", "coordinates", "world", "object");
        coordinatesBox.initialize(data);
        result.addGraphBoxPart(coordinatesBox);
        return result;
    }
}