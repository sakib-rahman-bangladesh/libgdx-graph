package com.gempukku.libgdx.graph.pipeline.producer.math.exponential;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.InverseSquareRootPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class InverseSquareRootPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public InverseSquareRootPipelineNodeProducer() {
        super(new InverseSquareRootPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return 1f / (float) Math.sqrt(value);
    }
}
