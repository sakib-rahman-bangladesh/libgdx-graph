package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    protected NodeConfiguration<PipelineFieldType> configuration;

    public PipelineNodeProducerImpl(NodeConfiguration<PipelineFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputFields) {
        ObjectMap<String, PipelineNode.FieldOutput<?>> singleInputs = new ObjectMap<>();
        for (ObjectMap.Entry<String, Array<PipelineNode.FieldOutput<?>>> entry : inputFields.entries()) {
            if (entry.value != null && entry.value.size == 1)
                singleInputs.put(entry.key, entry.value.get(0));
        }

        return createNodeForSingleInputs(data, singleInputs);
    }

    protected abstract PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields);
}
