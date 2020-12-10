package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class Vector3ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector3;
    }

    @Override
    public PropertySource createProperty(String name, JsonValue data, boolean designTime) {
        return new PropertySource(name, ShaderFieldType.Vector3, ShaderFieldType.Vector3.convertFromJson(data));
    }
}
