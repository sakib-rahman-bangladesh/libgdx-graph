package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.LengthPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class NormalizePipelineNodeProducer extends PipelineNodeProducerImpl {
    public NormalizePipelineNodeProducer() {
        super(new LengthPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object a = aFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (a instanceof Float) {
                    result = Math.signum((float) a);
                } else if (a instanceof Vector2) {
                    result = ((Vector2) a).cpy().nor();
                } else if (a instanceof Vector3) {
                    result = ((Vector3) a).cpy().nor();
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;

                    float length = (float) Math.sqrt(
                            aColor.r * aColor.r +
                                    aColor.g * aColor.g +
                                    aColor.b * aColor.b +
                                    aColor.a * aColor.a);
                    result = aColor.cpy().set(aColor.r / length, aColor.b / length, aColor.b / length, aColor.a / length);
                } else {
                    throw new IllegalArgumentException("Not matching type for function");
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

}
