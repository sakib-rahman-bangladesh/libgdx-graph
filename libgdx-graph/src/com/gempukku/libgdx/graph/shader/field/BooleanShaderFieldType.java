package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;

public class BooleanShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Boolean;
    }

    @Override
    public String getShaderType() {
        return "bool";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Boolean";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return null;
    }
}
