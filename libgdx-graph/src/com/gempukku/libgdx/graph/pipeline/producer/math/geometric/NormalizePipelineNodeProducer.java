package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.NormalizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class NormalizePipelineNodeProducer extends PipelineNodeProducerImpl {
    public NormalizePipelineNodeProducer() {
        super(new NormalizePipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("input");
        final PipelineFieldType returnType = aFunction.getPropertyType();

        final Object result = createResult(returnType);

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object a = aFunction.getValue(pipelineRenderingContext, null);
                Object returnValue;

                if (returnType == PipelineFieldType.Float) {
                    returnValue = Math.signum((float) a);
                } else if (returnType == PipelineFieldType.Vector2) {
                    returnValue = ((Vector2) result).set((Vector2) a).nor();
                } else if (returnType == PipelineFieldType.Vector3) {
                    returnValue = ((Vector3) result).set((Vector3) a).nor();
                } else {
                    Color aColor = (Color) a;

                    float length = (float) Math.sqrt(
                            aColor.r * aColor.r +
                                    aColor.g * aColor.g +
                                    aColor.b * aColor.b +
                                    aColor.a * aColor.a);
                    returnValue = ((Color) result).set(
                            aColor.r / length,
                            aColor.b / length,
                            aColor.b / length,
                            aColor.a / length);
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(returnValue);
            }
        };
    }

    private Object createResult(PipelineFieldType returnType) {
        if (returnType == PipelineFieldType.Float) {
            return 0f;
        } else if (returnType == PipelineFieldType.Vector2) {
            return new Vector2();
        } else if (returnType == PipelineFieldType.Vector3) {
            return new Vector3();
        } else if (returnType == PipelineFieldType.Color) {
            return new Color();
        } else {
            throw new IllegalArgumentException("Not matching type for function");
        }
    }
}
