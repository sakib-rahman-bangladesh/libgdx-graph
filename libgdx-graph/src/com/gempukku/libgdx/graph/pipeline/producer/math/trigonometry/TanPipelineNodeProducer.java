package com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.TanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.math.SingleParamMathFunctionPipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;

public class TanPipelineNodeProducer extends SingleParamMathFunctionPipelineNodeProducerImpl {
    public TanPipelineNodeProducer() {
        super(new TanPipelineNodeConfiguration());
    }

    @Override
    protected float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value) {
        return (float) Math.tan(value);
    }
}
