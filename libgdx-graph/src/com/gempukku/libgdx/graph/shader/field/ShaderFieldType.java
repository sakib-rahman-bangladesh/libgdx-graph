package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;

public interface ShaderFieldType extends FieldType {
    String Float = "Float";
    String Vector2 = "Vector2";
    String Vector3 = "Vector3";
    String Vector4 = "Vector4";
    String Boolean = "Boolean";
    String TextureRegion = "TextureRegion";

    String getShaderType();

    Object convertFromJson(JsonValue data);
}
