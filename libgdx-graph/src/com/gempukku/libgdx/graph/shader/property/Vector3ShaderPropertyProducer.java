package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector3ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector3;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, boolean designTime) {
        return new PropertySource(index, name, ShaderFieldType.Vector3, ShaderFieldType.Vector3.convertFromJson(data));
    }
}
