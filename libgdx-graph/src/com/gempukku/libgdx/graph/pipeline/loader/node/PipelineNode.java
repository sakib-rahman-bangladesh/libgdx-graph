package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;

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
