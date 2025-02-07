package com.gempukku.libgdx.graph.pipeline.producer.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public abstract class ThreeParamMathFunctionPipelineNodeProducer extends PipelineNodeProducerImpl {
    private String param1;
    private String param2;
    private String param3;
    private String outputName;

    public ThreeParamMathFunctionPipelineNodeProducer(NodeConfiguration configuration,
                                                      String param1, String param2, String param3, String outputName) {
        super(configuration);
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.outputName = outputName;
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get(param1);
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get(param2);
        final PipelineNode.FieldOutput<?> cFunction = inputFields.get(param3);

        final String returnType = aFunction.getPropertyType();

        final Object result = createResult(returnType);

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object a = aFunction.getValue(pipelineRenderingContext, null);
                Object b = bFunction.getValue(pipelineRenderingContext, null);
                Object c = cFunction.getValue(pipelineRenderingContext, null);

                Object resultValue;
                if (returnType.equals(PipelineFieldType.Float)) {
                    resultValue = executeFunction(a, b, c, 0);
                } else if (returnType.equals(PipelineFieldType.Vector2)) {
                    resultValue = ((Vector2) result).set(
                            executeFunction(a, b, c, 0),
                            executeFunction(a, b, c, 1));
                } else if (returnType.equals(PipelineFieldType.Vector3)) {
                    resultValue = ((Vector3) result).set(
                            executeFunction(a, b, c, 0),
                            executeFunction(a, b, c, 1),
                            executeFunction(a, b, c, 2)
                    );
                } else {
                    resultValue = ((Color) result).set(
                            executeFunction(a, b, c, 0),
                            executeFunction(a, b, c, 1),
                            executeFunction(a, b, c, 2),
                            executeFunction(a, b, c, 3));
                }

                OutputValue output = outputValues.get(outputName);
                if (output != null)
                    output.setValue(resultValue);
            }
        };
    }

    private float executeFunction(Object a, Object b, Object c, int index) {
        return executeFunction(getParamValue(a, index), getParamValue(b, index), getParamValue(c, index));
    }

    private Object createResult(String returnType) {
        if (returnType.equals(PipelineFieldType.Float)) {
            return 0f;
        } else if (returnType.equals(PipelineFieldType.Vector2)) {
            return new Vector2();
        } else if (returnType.equals(PipelineFieldType.Vector3)) {
            return new Vector3();
        } else if (returnType.equals(PipelineFieldType.Color)) {
            return new Color();
        } else {
            throw new IllegalArgumentException("Not matching type for function");
        }
    }

    private float getParamValue(Object value, int index) {
        if (value instanceof Float) {
            return (float) value;
        } else if (value instanceof Vector2) {
            Vector2 v2 = (Vector2) value;
            return index == 0 ? v2.x : v2.y;
        } else if (value instanceof Vector3) {
            Vector3 v3 = (Vector3) value;
            return index == 0 ? v3.x : (index == 1 ? v3.y : v3.z);
        } else if (value instanceof Color) {
            Color c = (Color) value;
            return index == 0 ? c.r : (index == 1 ? c.g : (index == 2 ? c.b : c.a));
        }
        throw new IllegalArgumentException("Unknown type of value");
    }

    protected abstract float executeFunction(float a, float b, float c);
}
