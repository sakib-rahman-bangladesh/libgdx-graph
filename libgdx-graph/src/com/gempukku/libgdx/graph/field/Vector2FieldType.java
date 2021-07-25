package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector2FieldType implements ShaderFieldType, PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Vector2;
    }

    @Override
    public String getShaderType() {
        return "vec2";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Vector2";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        return new com.badlogic.gdx.math.Vector2(x, y);
    }
}
