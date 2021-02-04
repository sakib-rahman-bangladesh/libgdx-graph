package com.gempukku.libgdx.graph.pipeline.loader.math.arithmetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.ReciprocalPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class ReciprocalPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ReciprocalPipelineNodeProducer() {
        super(new ReciprocalPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> inputFunction = inputFields.get("input");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object value = inputFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (inputFunction.getPropertyType() == PipelineFieldType.Float) {
                    result = 1 / (float) value;
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector2) {
                    Vector2 x = (Vector2) value;
                    result = x.cpy().set(1 / x.x, 1 / x.y);
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector3) {
                    Vector3 x = (Vector3) value;
                    result = x.cpy().set(1 / x.x, 1 / x.y, 1 / x.z);
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Color) {
                    Color x = (Color) value;
                    result = x.cpy().set(1 / x.r, 1 / x.g, 1 / x.b, 1 / x.a);
                } else {
                    throw new IllegalArgumentException("Not matching type for reciprocal");
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }
}
