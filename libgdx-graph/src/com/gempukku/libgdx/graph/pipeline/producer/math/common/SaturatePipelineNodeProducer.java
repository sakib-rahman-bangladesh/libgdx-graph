package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SaturatePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class SaturatePipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public SaturatePipelineNodeProducer() {
        super(new SaturatePipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return MathUtils.clamp(value, 0, 1);
    }
}
