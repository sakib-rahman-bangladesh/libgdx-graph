package com.gempukku.libgdx.graph.pipeline.loader;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class BooleanFieldOutput implements PipelineNode.FieldOutput<Boolean> {
    private boolean value;

    public BooleanFieldOutput(boolean value) {
        this.value = value;
    }

    @Override
    public PipelineFieldType getPropertyType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public Boolean getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements) {
        return value;
    }
}
