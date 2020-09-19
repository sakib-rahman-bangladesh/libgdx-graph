package com.gempukku.libgdx.graph.ui.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.IndexBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;


public class IndexedBoxProducer<T extends FieldType> extends GraphBoxProducerImpl<T> {
    public IndexedBoxProducer(NodeConfiguration<T> configuration) {
        super(configuration);
    }

    @Override
    public GraphBoxImpl<T> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<T> result = super.createPipelineGraphBox(skin, id, data);
        IndexBoxPart<T> indexPart = new IndexBoxPart<>(skin, "Index", "index");
        if (data != null)
            indexPart.initialize(data);
        result.addGraphBoxPart(indexPart);
        return result;
    }
}
