package com.gempukku.libgdx.graph.pipeline.producer.math.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.common.ClampPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class ClampPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ClampPipelineNodeProducer() {
        super(new ClampPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> inputFunction = inputFields.get("input");
        final PipelineNode.FieldOutput<?> minFunction = inputFields.get("min");
        final PipelineNode.FieldOutput<?> maxFunction = inputFields.get("max");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object value = inputFunction.getValue(pipelineRenderingContext, null);
                float min = (float) minFunction.getValue(pipelineRenderingContext, null);
                float max = (float) maxFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (inputFunction.getPropertyType() == PipelineFieldType.Float) {
                    result = executeFunction(min, max, (float) value);
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector2) {
                    Vector2 x = (Vector2) value;
                    result = x.cpy().set(executeFunction(min, max, x.x), executeFunction(min, max, x.y));
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector3) {
                    Vector3 x = (Vector3) value;
                    result = x.cpy().set(executeFunction(min, max, x.x), executeFunction(min, max, x.y), executeFunction(min, max, x.z));
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Color) {
                    Color x = (Color) value;
                    result = x.cpy().set(executeFunction(min, max, x.r), executeFunction(min, max, x.g), executeFunction(min, max, x.b), executeFunction(min, max, x.a));
                } else {
                    throw new IllegalArgumentException("Not matching type for function");
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private float executeFunction(float min, float max, float value) {
        return MathUtils.clamp(value, min, max);
    }
}
