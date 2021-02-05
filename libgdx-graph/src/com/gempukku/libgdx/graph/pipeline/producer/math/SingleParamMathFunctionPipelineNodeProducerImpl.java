package com.gempukku.libgdx.graph.pipeline.producer.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public abstract class SingleParamMathFunctionPipelineNodeProducerImpl extends PipelineNodeProducerImpl {
    private String inputName;
    private String outputName;

    public SingleParamMathFunctionPipelineNodeProducerImpl(NodeConfiguration<PipelineFieldType> configuration) {
        this(configuration, "input", "output");
    }

    public SingleParamMathFunctionPipelineNodeProducerImpl(NodeConfiguration<PipelineFieldType> configuration, String inputName, String outputName) {
        super(configuration);
        this.inputName = inputName;
        this.outputName = outputName;
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> inputFunction = inputFields.get(inputName);
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object value = inputFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (inputFunction.getPropertyType() == PipelineFieldType.Float) {
                    result = executeFunction(data, inputFields, (float) value);
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector2) {
                    Vector2 x = (Vector2) value;
                    result = x.cpy().set(executeFunction(data, inputFields, x.x), executeFunction(data, inputFields, x.y));
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Vector3) {
                    Vector3 x = (Vector3) value;
                    result = x.cpy().set(executeFunction(data, inputFields, x.x), executeFunction(data, inputFields, x.y), executeFunction(data, inputFields, x.z));
                } else if (inputFunction.getPropertyType() == PipelineFieldType.Color) {
                    Color x = (Color) value;
                    result = x.cpy().set(executeFunction(data, inputFields, x.r), executeFunction(data, inputFields, x.g), executeFunction(data, inputFields, x.b), executeFunction(data, inputFields, x.a));
                } else {
                    throw new IllegalArgumentException("Not matching type for function");
                }

                OutputValue output = outputValues.get(outputName);
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    protected abstract float executeFunction(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields, float value);
}
