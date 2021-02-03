package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.CalculateLightingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class CalculateLightingBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public CalculateLightingBoxProducer() {
        super(new CalculateLightingShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);

        StringBoxPart<ShaderFieldType> envId = new StringBoxPart<>(skin, "Env id: ", "id");
        envId.initialize(data);
        result.addGraphBoxPart(envId);

        addConfigurationInputsAndOutputs(skin, result);
        return result;
    }
}