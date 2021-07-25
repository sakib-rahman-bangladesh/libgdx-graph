package com.gempukku.libgdx.graph.shader.field;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.field.BooleanFieldType;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.field.Vector2FieldType;
import com.gempukku.libgdx.graph.field.Vector3FieldType;

public interface ShaderFieldType extends FieldType {
    ShaderFieldType Float = new FloatFieldType();
    ShaderFieldType Vector2 = new Vector2FieldType();
    ShaderFieldType Vector3 = new Vector3FieldType();
    ShaderFieldType Vector4 = new Vector4ShaderFieldType();
    ShaderFieldType Boolean = new BooleanFieldType();
    ShaderFieldType TextureRegion = new TextureRegionShaderFieldType();

    String getShaderType();

    Object convertFromJson(JsonValue data);
}
