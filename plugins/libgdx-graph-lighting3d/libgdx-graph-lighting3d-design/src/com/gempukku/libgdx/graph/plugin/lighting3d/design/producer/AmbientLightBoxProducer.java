package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.AmbientLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class AmbientLightBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public AmbientLightBoxProducer() {
        super(new AmbientLightShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(id);

        StringBoxPart<ShaderFieldType> envId = new StringBoxPart<>("Env id: ", "id");
        if (data != null)
            envId.initialize(data, "");
        result.addGraphBoxPart(envId);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}