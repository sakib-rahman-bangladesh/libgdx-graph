package com.gempukku.libgdx.graph.plugin.models.design.producer.material;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.material.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class FloatAttributeBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public FloatAttributeBoxProducer(String type, String name) {
        super(new FloatAttributeShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        FloatBoxPart<ShaderFieldType> colorPart = new FloatBoxPart<>("Default", "default", 0f, null);
        if (data != null)
            colorPart.initialize(data);
        result.addGraphBoxPart(colorPart);
        return result;
    }
}
