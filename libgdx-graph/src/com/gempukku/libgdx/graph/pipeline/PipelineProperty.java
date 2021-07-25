package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;

public interface PipelineProperty {
    PipelineFieldType getType();

    Object getValue();
}
