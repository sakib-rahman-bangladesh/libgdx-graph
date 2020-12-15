package com.gempukku.libgdx.graph.ui.shader.particles.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleRandomShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ParticleRandomShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public ParticleRandomShaderBoxProducer() {
        super(new ParticleRandomShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        float min = 0f;
        float max = 1f;
        if (data != null) {
            min = data.getFloat("min", min);
            max = data.getFloat("max", max);
        }

        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        FloatBoxPart<ShaderFieldType> minPart = new FloatBoxPart<ShaderFieldType>(skin, "Minimum", "min", min, null);
        result.addGraphBoxPart(minPart);
        FloatBoxPart<ShaderFieldType> maxPart = new FloatBoxPart<ShaderFieldType>(skin, "Maximum", "max", max, null);
        result.addGraphBoxPart(maxPart);

        addConfigurationInputsAndOutputs(skin, result);
        return result;
    }
}
