package com.gempukku.libgdx.graph.pipeline.loader.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameMultipleInputsJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;

public class MultiplyPipelineNodeProducer extends PipelineNodeProducerImpl {
    public MultiplyPipelineNodeProducer() {
        super(new MultiplyPipelineNodeConfiguration());
    }


    @Override
    protected PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return null;
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputFields) {
        final Array<PipelineNode.FieldOutput<?>> inputs = inputFields.get("inputs");
        final PipelineFieldType resultType = determineOutputType(inputs);
        return new OncePerFrameMultipleInputsJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object result = createDefaultValue(resultType);
                for (FieldOutput<?> input : inputs) {
                    Object value = input.getValue(pipelineRenderingContext, null);
                    result = multiply(result, value);
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private Object multiply(Object obj, Object value) {
        if (value instanceof Float) {
            float f = (float) value;
            if (obj instanceof Float)
                return (float) obj * f;
            if (obj instanceof Color) {
                return ((Color) obj).mul(f, f, f, f);
            }
            if (obj instanceof Vector2) {
                return ((Vector2) obj).scl(f, f);
            }
            if (obj instanceof Vector3) {
                return ((Vector3) obj).scl(f);
            }
        } else {
            if (obj instanceof Color)
                return ((Color) obj).mul((Color) value);
            if (obj instanceof Vector2)
                return ((Vector2) obj).scl((Vector2) value);
            if (obj instanceof Vector3)
                return ((Vector3) obj).scl((Vector3) value);
        }
        return null;
    }

    private Object createDefaultValue(PipelineFieldType type) {
        if (type == PipelineFieldType.Float)
            return 1f;
        else if (type == PipelineFieldType.Vector2)
            return new Vector2(1f, 1f);
        else if (type == PipelineFieldType.Vector3)
            return new Vector3(1f, 1f, 1f);
        else
            return new Color(1, 1, 1, 1);
    }

    private PipelineFieldType determineOutputType(Array<PipelineNode.FieldOutput<?>> inputs) {
        PipelineFieldType result = PipelineFieldType.Float;
        for (PipelineNode.FieldOutput<?> input : inputs) {
            PipelineFieldType fieldType = input.getPropertyType();
            if (fieldType != result && (result != PipelineFieldType.Float && fieldType != PipelineFieldType.Float))
                throw new IllegalStateException("Invalid mix of input field types");
            if (fieldType != PipelineFieldType.Float)
                result = fieldType;
        }
        return result;
    }
}
