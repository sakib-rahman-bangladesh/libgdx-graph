package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.ExponentialPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class ExponentialPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ExponentialPipelineNodeProducer() {
        super(new ExponentialPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return (float) Math.exp(value);
    }
}
