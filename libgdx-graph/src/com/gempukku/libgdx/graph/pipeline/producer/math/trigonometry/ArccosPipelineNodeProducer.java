package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.ArccosPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class ArccosPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ArccosPipelineNodeProducer() {
        super(new ArccosPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return MathUtils.acos(value);
    }
}
