package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.LogarithmBase2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class LogarithmBase2PipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public LogarithmBase2PipelineNodeProducer() {
        super(new LogarithmBase2PipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return MathUtils.log2(value);
    }
}
