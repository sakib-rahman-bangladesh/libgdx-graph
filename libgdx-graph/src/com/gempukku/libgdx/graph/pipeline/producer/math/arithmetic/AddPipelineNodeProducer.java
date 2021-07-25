package com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.AddPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameMultipleInputsJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;

public class AddPipelineNodeProducer extends PipelineNodeProducerImpl {
    public AddPipelineNodeProducer() {
        super(new AddPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return null;
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputFields) {
        final Array<PipelineNode.FieldOutput<?>> inputs = inputFields.get("inputs");
        final String resultType = determineOutputType(inputs);

        final Object result = createDefaultValue(resultType);

        return new OncePerFrameMultipleInputsJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object returnValue = resetDefaultValue(resultType, result);
                for (FieldOutput<?> input : inputs) {
                    Object value = input.getValue(pipelineRenderingContext, null);
                    returnValue = add(returnValue, value);
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(returnValue);
            }
        };
    }

    private Object add(Object obj, Object value) {
        if (value instanceof Float) {
            float f = (float) value;
            if (obj instanceof Float)
                return (float) obj + f;
            if (obj instanceof Color) {
                return ((Color) obj).add(f, f, f, f);
            }
            if (obj instanceof Vector2) {
                return ((Vector2) obj).add(f, f);
            }
            if (obj instanceof Vector3) {
                return ((Vector3) obj).add(f);
            }
        } else {
            if (obj instanceof Color)
                return ((Color) obj).add((Color) value);
            if (obj instanceof Vector2)
                return ((Vector2) obj).add((Vector2) value);
            if (obj instanceof Vector3)
                return ((Vector3) obj).add((Vector3) value);
        }
        return null;
    }

    private Object createDefaultValue(String type) {
        if (type.equals(PipelineFieldType.Float))
            return 0f;
        else if (type.equals(PipelineFieldType.Vector2))
            return new Vector2();
        else if (type.equals(PipelineFieldType.Vector3))
            return new Vector3();
        else
            return new Color(0, 0, 0, 0);
    }

    private Object resetDefaultValue(String type, Object value) {
        if (type.equals(PipelineFieldType.Float))
            return 0f;
        else if (type.equals(PipelineFieldType.Vector2))
            return ((Vector2) value).set(0f, 0f);
        else if (type.equals(PipelineFieldType.Vector3))
            return ((Vector3) value).set(0f, 0f, 0f);
        else
            return ((Color) value).set(0f, 0f, 0f, 0f);
    }

    private String determineOutputType(Array<PipelineNode.FieldOutput<?>> inputs) {
        String result = PipelineFieldType.Float;
        for (PipelineNode.FieldOutput<?> input : inputs) {
            String fieldType = input.getPropertyType();
            if (!fieldType.equals(result) && (!result.equals(PipelineFieldType.Float) && !fieldType.equals(PipelineFieldType.Float)))
                throw new IllegalStateException("Invalid mix of input field types");
            if (!fieldType.equals(PipelineFieldType.Float))
                result = fieldType;
        }
        return result;
    }
}
