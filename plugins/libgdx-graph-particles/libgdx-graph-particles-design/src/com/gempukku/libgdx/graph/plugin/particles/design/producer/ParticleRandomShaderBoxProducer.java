package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleRandomShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ParticleRandomShaderBoxProducer extends GraphBoxProducerImpl {
    public ParticleRandomShaderBoxProducer() {
        super(new ParticleRandomShaderNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        float min = 0f;
        float max = 1f;
        if (data != null) {
            min = data.getFloat("min", min);
            max = data.getFloat("max", max);
        }

        GraphBoxImpl result = createGraphBox(id);
        FloatBoxPart minPart = new FloatBoxPart("Minimum", "min", min, null);
        result.addGraphBoxPart(minPart);
        FloatBoxPart maxPart = new FloatBoxPart("Maximum", "max", max, null);
        result.addGraphBoxPart(maxPart);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}
