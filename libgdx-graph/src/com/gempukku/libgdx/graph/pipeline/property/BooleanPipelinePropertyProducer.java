package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class BooleanPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        final boolean x = data.getBoolean("value");
        return new WritablePipelineProperty(PipelineFieldType.Boolean,
                new Supplier<Boolean>() {
                    @Override
                    public Boolean get() {
                        return x;
                    }
                });
    }
}
