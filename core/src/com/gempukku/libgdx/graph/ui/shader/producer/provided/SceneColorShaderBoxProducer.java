package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.TextureSettingsGraphBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class SceneColorShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public SceneColorShaderBoxProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        TextureSettingsGraphBoxPart<ShaderFieldType> textureSettingsPart = new TextureSettingsGraphBoxPart<>();
        if (data != null)
            textureSettingsPart.initialize(data);
        result.addGraphBoxPart(textureSettingsPart);

        return result;
    }
}
