package com.gempukku.libgdx.graph.pipeline.producer.math.value;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.value.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class SplitPipelineNodeProducer extends PipelineNodeProducerImpl {
    public SplitPipelineNodeProducer() {
        super(new SplitPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Object input = inputFields.get("input").getValue(pipelineRenderingContext, null);
                float x = 0f;
                float y = 0f;
                float z = 0f;
                float w = 0f;
                if (input instanceof Vector2) {
                    x = ((Vector2) input).x;
                    y = ((Vector2) input).y;
                } else if (input instanceof Vector3) {
                    x = ((Vector3) input).x;
                    y = ((Vector3) input).y;
                    z = ((Vector3) input).z;
                } else {
                    x = ((Color) input).r;
                    y = ((Color) input).g;
                    z = ((Color) input).b;
                    w = ((Color) input).a;
                }

                OutputValue<Float> outputX = outputValues.get("x");
                if (outputX != null)
                    outputX.setValue(x);
                OutputValue<Float> outputY = outputValues.get("y");
                if (outputY != null)
                    outputY.setValue(y);
                OutputValue<Float> outputZ = outputValues.get("z");
                if (outputZ != null)
                    outputZ.setValue(z);
                OutputValue<Float> outputW = outputValues.get("w");
                if (outputW != null)
                    outputW.setValue(w);
            }
        };
    }
}
