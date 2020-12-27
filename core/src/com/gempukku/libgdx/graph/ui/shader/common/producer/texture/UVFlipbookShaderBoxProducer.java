package com.gempukku.libgdx.graph.ui.shader.common.producer.texture;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class UVFlipbookShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public UVFlipbookShaderBoxProducer() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        CheckboxBoxPart<ShaderFieldType> invertX = new CheckboxBoxPart<>(skin, "Invert X", "invertX");
        invertX.initialize(data);
        result.addGraphBoxPart(invertX);
        CheckboxBoxPart<ShaderFieldType> invertY = new CheckboxBoxPart<>(skin, "Invert Y", "invertY");
        invertY.initialize(data);
        result.addGraphBoxPart(invertY);

        return result;
    }
}
