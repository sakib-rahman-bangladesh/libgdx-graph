package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    private ShaderFieldType type = new FloatFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, boolean designTime) {
        return new PropertySource(index, name, type, type.convertFromJson(data));
    }
}
