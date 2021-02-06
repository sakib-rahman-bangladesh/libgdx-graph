package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.ArcsinPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class ArcsinPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public ArcsinPipelineNodeProducer() {
        super(new ArcsinPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return MathUtils.asin(value);
    }
}
