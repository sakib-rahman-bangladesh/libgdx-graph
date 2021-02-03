package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.producer.shader.ParticlesShadersBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ParticlesShaderRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public ParticlesShaderRendererBoxProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        ParticlesShadersBoxPart graphBoxPart = new ParticlesShadersBoxPart(skin);
        graphBoxPart.initialize(data);
        result.addGraphBoxPart(graphBoxPart);
        return result;
    }
}
