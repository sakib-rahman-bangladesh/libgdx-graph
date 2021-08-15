package com.gempukku.libgdx.graph.ui.shader.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.attribute.Vector2AttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector2BoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class Vector2AttributeBoxProducer extends GraphBoxProducerImpl {
    public Vector2AttributeBoxProducer() {
        super(new Vector2AttributeShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        StringBoxPart nameBoxPart = new StringBoxPart("Attribute", "name");
        Vector2BoxPart valueBoxPart = new Vector2BoxPart("Default", "x", "y", 0, 0, null, null);
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
