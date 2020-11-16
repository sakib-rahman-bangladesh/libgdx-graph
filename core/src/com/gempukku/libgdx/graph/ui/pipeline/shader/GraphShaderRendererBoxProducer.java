package com.gempukku.libgdx.graph.ui.pipeline.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class GraphShaderRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public GraphShaderRendererBoxProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        GraphShadersBoxPart graphBoxPart = new GraphShadersBoxPart(skin);
        graphBoxPart.initialize(data);
        result.addGraphBoxPart(graphBoxPart);
        return result;
    }

    @Override
    public GraphBox<PipelineFieldType> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }
}
