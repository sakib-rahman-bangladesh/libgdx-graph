package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Float;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, boolean designTime) {
        return new PropertySource(index, name, ShaderFieldType.Float, ShaderFieldType.Float.convertFromJson(data));
    }
}
