package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;

public enum ShaderFieldType implements FieldType {
    Float("float"), Vector2("vec2"), Vector3("vec3"), Vector4("vec4"), Boolean("bool"),
    TextureRegion("vec4");

    private String shaderType;

    ShaderFieldType(String shaderType) {
        this.shaderType = shaderType;
    }

    @Override
    public boolean accepts(Object value) {
        switch (this) {
            case Float:
                return value instanceof Float;
            case Vector2:
                return value instanceof com.badlogic.gdx.math.Vector2;
            case Vector3:
                return value instanceof com.badlogic.gdx.math.Vector3;
            case Vector4:
                return value instanceof com.badlogic.gdx.graphics.Color;
            case Boolean:
                return value instanceof Boolean;
            case TextureRegion:
                return value instanceof TextureRegion;
        }
        return false;
    }

    public String getShaderType() {
        return shaderType;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isTexture() {
        return this == TextureRegion;
    }

    public Object convertFromJson(JsonValue data) {
        if (this == Float)
            return data.getFloat("x");
        if (this == Vector2) {
            final float x = data.getFloat("x");
            final float y = data.getFloat("y");
            return new com.badlogic.gdx.math.Vector2(x, y);
        }
        if (this == Vector3) {
            final float x = data.getFloat("x");
            final float y = data.getFloat("y");
            final float z = data.getFloat("z");
            return new com.badlogic.gdx.math.Vector3(x, y, z);
        }
        if (this == Vector4)
            return Color.valueOf(data.getString("color"));
        if (this == TextureRegion) {
            if (data == null)
                return null;
            return data.getString("previewPath", null);
        }

        return null;
    }
}
