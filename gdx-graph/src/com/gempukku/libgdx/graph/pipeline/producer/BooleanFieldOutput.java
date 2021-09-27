package com.gempukku.libgdx.graph.pipeline.producer;

import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class BooleanFieldOutput implements PipelineNode.FieldOutput<Boolean> {
    private boolean value;

    public BooleanFieldOutput(boolean value) {
        this.value = value;
    }

    @Override
    public String getPropertyType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public Boolean getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements) {
        return value;
    }
}
