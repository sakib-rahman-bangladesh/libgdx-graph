package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector2ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector2;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, boolean designTime) {
        return new PropertySource(index, name, ShaderFieldType.Vector2, ShaderFieldType.Vector2.convertFromJson(data));
    }
}
