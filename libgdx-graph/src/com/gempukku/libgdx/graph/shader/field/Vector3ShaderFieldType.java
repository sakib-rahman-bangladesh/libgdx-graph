package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;

public class Vector3ShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.math.Vector3;
    }

    @Override
    public String getShaderType() {
        return "vec3";
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return "Vector3";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        final float z = data.getFloat("z");
        return new com.badlogic.gdx.math.Vector3(x, y, z);
    }
}
