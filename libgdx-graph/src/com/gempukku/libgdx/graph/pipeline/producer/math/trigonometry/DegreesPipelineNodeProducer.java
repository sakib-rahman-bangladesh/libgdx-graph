package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.DegreesPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class DegreesPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public DegreesPipelineNodeProducer() {
        super(new DegreesPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return MathUtils.radiansToDegrees * value;
    }
}
