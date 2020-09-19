package com.gempukku.libgdx.graph.ui.shader.material;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.material.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;


public class FloatAttributeBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public FloatAttributeBoxProducer(String type, String name) {
        super(new FloatAttributeShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = super.createPipelineGraphBox(skin, id, data);
        FloatBoxPart<ShaderFieldType> colorPart = new FloatBoxPart<>(skin, "Default", "default");
        if (data != null)
            colorPart.initialize(data);
        result.addGraphBoxPart(colorPart);
        return result;
    }
}
