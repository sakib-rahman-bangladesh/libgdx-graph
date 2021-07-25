package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;

public class Vector2ShaderFieldType implements ShaderFieldType {
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
