package com.gempukku.libgdx.graph.pipeline.producer.rendering.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.node.EndPipelineNode;

public class EndPipelineNodeProducer extends PipelineNodeProducerImpl {
    public EndPipelineNodeProducer() {
        super(new EndPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new EndPipelineNode(
                (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input"));
    }
}
