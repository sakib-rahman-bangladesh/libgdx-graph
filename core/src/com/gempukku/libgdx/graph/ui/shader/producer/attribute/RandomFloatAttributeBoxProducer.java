package com.gempukku.libgdx.graph.ui.shader.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.attribute.RandomFloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector2BoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class RandomFloatAttributeBoxProducer extends GraphBoxProducerImpl {
    public RandomFloatAttributeBoxProducer() {
        super(new RandomFloatAttributeShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        StringBoxPart nameBoxPart = new StringBoxPart("Attribute", "name");
        Vector2BoxPart rangeBoxPart = new Vector2BoxPart("Range", "min", "max", 0, 1,
                null, null);
        result.addGraphBoxPart(nameBoxPart);
        result.addGraphBoxPart(rangeBoxPart);
        if (data != null) {
            nameBoxPart.initialize(data);
            rangeBoxPart.initialize(data);
        }
        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
