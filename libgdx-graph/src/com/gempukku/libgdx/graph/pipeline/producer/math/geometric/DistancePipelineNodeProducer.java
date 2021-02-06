package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.DistancePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class DistancePipelineNodeProducer extends PipelineNodeProducerImpl {
    public DistancePipelineNodeProducer() {
        super(new DistancePipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("p0");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("p1");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object a = aFunction.getValue(pipelineRenderingContext, null);
                Object b = bFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (a instanceof Float) {
                    result = Math.abs(((float) a) - ((float) b));
                } else if (a instanceof Vector2) {
                    result = ((Vector2) a).dst((Vector2) b);
                } else if (a instanceof Vector3) {
                    result = ((Vector3) a).dst((Vector3) b);
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;
                    Color bColor = (Color) b;

                    final float p1 = aColor.r - bColor.r;
                    final float p2 = aColor.g - bColor.g;
                    final float p3 = aColor.b - bColor.b;
                    final float p4 = aColor.a - bColor.a;
                    result = (float) Math.sqrt(p1 * p1 + p2 * p2 + p3 * p3 + p4 * p4);
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
