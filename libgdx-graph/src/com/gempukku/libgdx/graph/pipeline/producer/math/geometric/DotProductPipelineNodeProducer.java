package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.DotProductPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class DotProductPipelineNodeProducer extends PipelineNodeProducerImpl {
    public DotProductPipelineNodeProducer() {
        super(new DotProductPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("a");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("b");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object a = aFunction.getValue(pipelineRenderingContext, null);
                Object b = bFunction.getValue(pipelineRenderingContext, null);

                Object result;
                if (a instanceof Float) {
                    result = ((float) a) * ((float) b);
                } else if (a instanceof Vector2) {
                    result = ((Vector2) a).dot((Vector2) b);
                } else if (a instanceof Vector3) {
                    result = ((Vector3) a).dot((Vector3) b);
                } else if (a instanceof Color) {
                    Color aColor = (Color) a;
                    Color bColor = (Color) b;

                    result = aColor.r * bColor.r + aColor.g * bColor.g
                            + aColor.b * bColor.b + aColor.a * bColor.a;
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
