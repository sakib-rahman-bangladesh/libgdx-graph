package com.gempukku.libgdx.graph.ui.shader.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.attribute.Vector3AttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector3BoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class Vector3AttributeBoxProducer extends GraphBoxProducerImpl {
    public Vector3AttributeBoxProducer() {
        super(new Vector3AttributeShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        StringBoxPart nameBoxPart = new StringBoxPart("Attribute", "name");
        Vector3BoxPart valueBoxPart = new Vector3BoxPart("Default",
                "x", "y", "z", 0, 0, 0, null, null, null);
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
