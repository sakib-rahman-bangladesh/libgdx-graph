package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import org.json.simple.JSONObject;

import java.util.function.Supplier;

public class CameraPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Camera;
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.Camera,
                new Supplier<Camera>() {
                    @Override
                    public Camera get() {
                        return null;
                    }
                });
    }
}
