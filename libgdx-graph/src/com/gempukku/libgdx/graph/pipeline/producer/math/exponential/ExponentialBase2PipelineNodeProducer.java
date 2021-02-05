package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.ExponentialBase2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class ExponentialBase2PipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ExponentialBase2PipelineNodeProducer() {
        super(new ExponentialBase2PipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return (float) Math.pow(2, value);
    }
}
