package com.gempukku.libgdx.graph.plugin.ui.design;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.ui.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.StringBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class UIRendererBoxProducer extends GraphBoxProducerImpl {
    public UIRendererBoxProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);

        StringBoxPart stageId = new StringBoxPart("Stage id: ", "id");
        if (data != null)
            stageId.initialize(data, "");
        result.addGraphBoxPart(stageId);

        addConfigurationInputsAndOutputs(result);
        return result;
    }
}