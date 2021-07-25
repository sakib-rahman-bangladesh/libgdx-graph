package com.gempukku.libgdx.graph.pipeline.field;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.field.BooleanFieldType;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.field.Vector2FieldType;
import com.gempukku.libgdx.graph.field.Vector3FieldType;

public interface PipelineFieldType extends FieldType {
    PipelineFieldType Float = new FloatFieldType();
    PipelineFieldType Vector2 = new Vector2FieldType();
    PipelineFieldType Vector3 = new Vector3FieldType();
    PipelineFieldType Color = new ColorPipelineFieldType();
    PipelineFieldType Boolean = new BooleanFieldType();
    PipelineFieldType Camera = new CameraPipelineFieldType();
    PipelineFieldType RenderPipeline = new RenderPipelineFieldType();
    PipelineFieldType Callback = new CallbackPipelineFieldType();
}
