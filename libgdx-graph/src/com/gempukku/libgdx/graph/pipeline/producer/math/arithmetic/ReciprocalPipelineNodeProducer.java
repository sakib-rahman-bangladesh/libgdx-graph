package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.ReciprocalPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class ReciprocalPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ReciprocalPipelineNodeProducer() {
        super(new ReciprocalPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return 1 / value;
    }
}
