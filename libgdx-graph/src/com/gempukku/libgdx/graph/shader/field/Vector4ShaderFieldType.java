package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;

public class Vector4ShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.graphics.Color;
    }

    @Override
    public String getShaderType() {
        return "vec4";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Vector4";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return Color.valueOf(data.getString("color"));
    }
}
