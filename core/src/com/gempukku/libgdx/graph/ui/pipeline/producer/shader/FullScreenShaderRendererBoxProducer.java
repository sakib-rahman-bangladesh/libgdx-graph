package com.gempukku.libgdx.graph.ui.pipeline.producer.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class FullScreenShaderRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public FullScreenShaderRendererBoxProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        StringBoxPart<PipelineFieldType> nameBoxPart = new StringBoxPart<>(skin, "Name", "name");
        if (data != null)
            nameBoxPart.initialize(data);
        result.addGraphBoxPart(nameBoxPart);
        return result;
    }

    @Override
    public GraphBox<PipelineFieldType> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }
}
