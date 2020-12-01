package com.gempukku.libgdx.graph.ui.pipeline.producer.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.rendering.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ScreenShaderRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public ScreenShaderRendererBoxProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);

        ScreenShaderBoxPart screenShaderBoxPart = new ScreenShaderBoxPart(skin);
        if (data != null)
            screenShaderBoxPart.initialize(data);
        result.addGraphBoxPart(screenShaderBoxPart);

        return result;
    }

    @Override
    public GraphBox<PipelineFieldType> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }


}
