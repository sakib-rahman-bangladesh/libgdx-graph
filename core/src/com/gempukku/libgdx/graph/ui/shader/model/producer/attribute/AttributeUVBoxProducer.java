package com.gempukku.libgdx.graph.ui.shader.model.producer.attribute;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.model.attribute.AttributeUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class AttributeUVBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public AttributeUVBoxProducer() {
        super(new AttributeUVShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        SelectBoxPart<ShaderFieldType> attributeUV = new SelectBoxPart<>(skin, "Channel", "channel", "UV0", "UV1", "UV2", "UV3");
        attributeUV.initialize(data);
        result.addGraphBoxPart(attributeUV);
        return result;
    }
}