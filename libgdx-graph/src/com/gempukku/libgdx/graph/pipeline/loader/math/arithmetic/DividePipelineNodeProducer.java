package com.gempukku.libgdx.graph.pipeline.loader.math.arithmetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.DividePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class DividePipelineNodeProducer extends PipelineNodeProducerImpl {
    public DividePipelineNodeProducer() {
        super(new DividePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("a");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("b");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object aValue = aFunction.getValue(pipelineRenderingContext, null);
                Object bValue = bFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (aValue instanceof Float && bValue instanceof Float) {
                    result = (float) aValue / (float) bValue;
                } else if (aValue instanceof Float) {
                    throw new IllegalArgumentException("Not matching types for subtract");
                } else if (bValue instanceof Float) {
                    result = divideFloat(aValue, (float) bValue);
                } else {
                    if (aValue.getClass() != bValue.getClass())
                        throw new IllegalArgumentException("Not matching types for subtract");
                    if (aValue instanceof Color) {
                        Color a = (Color) aValue;
                        Color b = (Color) bValue;
                        result = a.cpy().set(a.r / b.r, a.g / b.g, a.b / b.b, a.a / b.a);
                    } else if (aValue instanceof Vector2) {
                        Vector2 a = (Vector2) aValue;
                        Vector2 b = (Vector2) bValue;
                        result = a.cpy().set(a.x / b.x, a.y / b.y);
                    } else {
                        Vector3 a = (Vector3) aValue;
                        Vector3 b = (Vector3) bValue;
                        result = a.cpy().set(a.x / b.x, a.y / b.y, a.z / b.z);
                    }
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private Object divideFloat(Object aValue, float bValue) {
        if (aValue instanceof Color) {
            return ((Color) aValue).cpy().mul(1 / bValue, 1 / bValue, 1 / bValue, 1 / bValue);
        } else if (aValue instanceof Vector2) {
            return ((Vector2) aValue).cpy().scl(1 / bValue, 1 / bValue);
        } else {
            return ((Vector3) aValue).cpy().scl(1 / bValue, 1 / bValue, 1 / bValue);
        }
    }
}
