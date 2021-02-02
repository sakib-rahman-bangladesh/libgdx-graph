package com.gempukku.libgdx.graph.plugin.ui.design;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.plugin.ui.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class UIRendererBoxProducer extends GraphBoxProducerImpl<PipelineFieldType> {
    public UIRendererBoxProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<PipelineFieldType> result = createGraphBox(skin, id);

        StringBoxPart<PipelineFieldType> stageId = new StringBoxPart<>(skin, "Stage id: ", "id");
        stageId.initialize(data);
        result.addGraphBoxPart(stageId);

        addConfigurationInputsAndOutputs(skin, result);
        return result;
    }
}