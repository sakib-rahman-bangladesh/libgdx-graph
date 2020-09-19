package com.gempukku.libgdx.graph.ui.shader.material;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.material.ColorAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;


public class ColorAttributeBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public ColorAttributeBoxProducer(String type, String name) {
        super(new ColorAttributeShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = super.createPipelineGraphBox(skin, id, data);
        ColorBoxPart<ShaderFieldType> colorPart = new ColorBoxPart<>(skin, "Default", "default");
        if (data != null)
            colorPart.initialize(data);
        result.addGraphBoxPart(colorPart);
        return result;
    }
}
