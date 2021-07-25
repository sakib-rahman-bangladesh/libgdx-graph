package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public interface PipelineNode {
    FieldOutput<?> getFieldOutput(String name);

    void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback);

    void startFrame();

    void endFrame();

    void dispose();

    interface FieldOutput<T> {
        PipelineFieldType getPropertyType();

        T getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements);
    }
}
