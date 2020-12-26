package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class PropertySource {
    private int propertyIndex;
    private String propertyName;
    private ShaderFieldType shaderFieldType;
    private Object defaultValue;

    public PropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType, Object defaultValue) {
        this.propertyIndex = propertyIndex;
        this.propertyName = propertyName;
        this.shaderFieldType = shaderFieldType;
        this.defaultValue = defaultValue;
    }

    public int getPropertyIndex() {
        return propertyIndex;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public ShaderFieldType getShaderFieldType() {
        return shaderFieldType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
