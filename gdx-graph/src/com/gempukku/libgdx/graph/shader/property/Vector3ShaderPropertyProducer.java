package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.field.Vector3FieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector3ShaderPropertyProducer implements GraphShaderPropertyProducer {
    private ShaderFieldType type = new Vector3FieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, boolean designTime) {
        return new PropertySource(index, name, type, location, type.convertFromJson(data));
    }
}
