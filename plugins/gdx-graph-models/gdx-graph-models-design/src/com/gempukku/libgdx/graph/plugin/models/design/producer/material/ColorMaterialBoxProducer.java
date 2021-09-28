package com.gempukku.libgdx.graph.plugin.models.design.producer.material;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.material.ColorMaterialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;


public class ColorMaterialBoxProducer extends GraphBoxProducerImpl {
    public ColorMaterialBoxProducer(String type, String name) {
        super(new ColorMaterialShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        ColorBoxPart colorPart = new ColorBoxPart("Default", "default");
        if (data != null)
            colorPart.initialize(data);
        result.addGraphBoxPart(colorPart);
        return result;
    }
}
