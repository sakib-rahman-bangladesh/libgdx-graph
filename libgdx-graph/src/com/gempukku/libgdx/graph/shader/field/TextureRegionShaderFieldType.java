package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;

public class TextureRegionShaderFieldType implements ShaderFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        return "TextureRegion";
    }

    @Override
    public boolean isTexture() {
        return true;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        if (data == null)
            return null;
        return data.getString("previewPath", null);
    }
}
