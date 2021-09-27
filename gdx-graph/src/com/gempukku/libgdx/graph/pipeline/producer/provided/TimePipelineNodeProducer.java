package com.gempukku.libgdx.graph.pipeline.producer.provided;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class TimePipelineNodeProducer extends PipelineNodeProducerImpl {
    public TimePipelineNodeProducer() {
        super(new TimePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                float timeValue = pipelineRenderingContext.getTimeProvider().getTime();
                OutputValue<Float> time = outputValues.get("time");
                if (time != null)
                    time.setValue(timeValue);
                OutputValue<Float> sinTime = outputValues.get("sinTime");
                if (sinTime != null)
                    sinTime.setValue(MathUtils.sin(timeValue));
                OutputValue<Float> cosTime = outputValues.get("cosTime");
                if (cosTime != null)
                    cosTime.setValue(MathUtils.cos(timeValue));
                OutputValue<Float> deltaTime = outputValues.get("deltaTime");
                if (deltaTime != null)
                    deltaTime.setValue(pipelineRenderingContext.getTimeProvider().getDelta());
            }
        };
    }
}
