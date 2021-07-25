package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;

public class FloatShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Float;
    }

    @Override
    public String getShaderType() {
        return "float";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Float";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return data.getFloat("x");
    }
}
