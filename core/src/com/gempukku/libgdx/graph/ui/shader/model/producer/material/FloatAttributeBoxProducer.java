package com.gempukku.libgdx.graph.ui.shader.model.producer.material;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.model.material.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;


public class FloatAttributeBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public FloatAttributeBoxProducer(String type, String name) {
        super(new FloatAttributeShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        FloatBoxPart<ShaderFieldType> colorPart = new FloatBoxPart<>(skin, "Default", "default", 0f, null);
        if (data != null)
            colorPart.initialize(data);
        result.addGraphBoxPart(colorPart);
        return result;
    }
}
