package com.gempukku.libgdx.graph.pipeline.field;

import com.gempukku.libgdx.graph.pipeline.CustomRenderCallback;

public class CallbackPipelineFieldType implements PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof CustomRenderCallback;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Callback";
    }

    @Override
    public boolean isTexture() {
        return false;
    }
}
