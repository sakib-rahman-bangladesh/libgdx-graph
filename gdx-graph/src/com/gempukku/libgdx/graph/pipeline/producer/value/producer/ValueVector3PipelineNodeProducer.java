package com.gempukku.libgdx.graph.pipeline.producer.value.producer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector3PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.value.node.ValuePipelineNode;

public class ValueVector3PipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueVector3PipelineNodeProducer() {
        super(new ValueVector3PipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", new Vector3(
                data.getFloat("v1"), data.getFloat("v2"), data.getFloat("v3")));
    }
}
