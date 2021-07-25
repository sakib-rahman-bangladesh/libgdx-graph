package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.PointLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.IndexBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class PointLightBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public PointLightBoxProducer() {
        super(new PointLightShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(id);

        StringBoxPart<ShaderFieldType> envId = new StringBoxPart<>("Env id: ", "id");
        if (data != null)
            envId.initialize(data, "");
        result.addGraphBoxPart(envId);

        addConfigurationInputsAndOutputs(result);

        IndexBoxPart<ShaderFieldType> indexPart = new IndexBoxPart<>("Index", "index");
        indexPart.initialize(data);
        result.addGraphBoxPart(indexPart);

        return result;
    }
}