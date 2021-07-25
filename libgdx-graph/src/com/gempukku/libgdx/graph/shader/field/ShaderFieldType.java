package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;

public interface ShaderFieldType extends FieldType {
    ShaderFieldType Float = new FloatShaderFieldType();
    ShaderFieldType Vector2 = new Vector2ShaderFieldType();
    ShaderFieldType Vector3 = new Vector3ShaderFieldType();
    ShaderFieldType Vector4 = new Vector4ShaderFieldType();
    ShaderFieldType Boolean = new BooleanShaderFieldType();
    ShaderFieldType TextureRegion = new TextureRegionShaderFieldType();

    String getShaderType();

    Object convertFromJson(JsonValue data);
}
