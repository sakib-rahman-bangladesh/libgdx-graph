package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class TimeShaderBoxProducer extends GraphBoxProducerImpl {
    public TimeShaderBoxProducer() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        float multiplier = 1f;
        if (data != null && data.has("multiplier"))
            multiplier = data.getFloat("multiplier");

        GraphBoxImpl result = createGraphBox(id);
        FloatBoxPart multiplierPart = new FloatBoxPart("Multiplier", "multiplier", multiplier, null);
        result.addGraphBoxPart(multiplierPart);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
