package com.gempukku.libgdx.graph.ui.shader.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.attribute.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class FloatAttributeBoxProducer extends GraphBoxProducerImpl {
    public FloatAttributeBoxProducer() {
        super(new FloatAttributeShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        StringBoxPart nameBoxPart = new StringBoxPart("Attribute", "name");
        FloatBoxPart valueBoxPart = new FloatBoxPart("Default", "default", 0, null);
        result.addGraphBoxPart(nameBoxPart);
        result.addGraphBoxPart(valueBoxPart);
        if (data != null) {
            nameBoxPart.initialize(data);
            valueBoxPart.initialize(data);
        }
        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
