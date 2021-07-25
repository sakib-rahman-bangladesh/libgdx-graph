package com.gempukku.libgdx.graph.plugin.models.design.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class AttributeUVBoxProducer extends GraphBoxProducerImpl {
    public AttributeUVBoxProducer() {
        super(new AttributeUVShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        SelectBoxPart attributeUV = new SelectBoxPart("Channel", "channel", "UV0", "UV1", "UV2", "UV3");
        attributeUV.initialize(data);
        result.addGraphBoxPart(attributeUV);
        return result;
    }
}