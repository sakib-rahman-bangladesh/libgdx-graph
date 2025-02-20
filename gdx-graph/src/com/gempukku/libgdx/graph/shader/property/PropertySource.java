package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class PropertySource {
    private int propertyIndex;
    private String propertyName;
    private ShaderFieldType shaderFieldType;
    private PropertyLocation location;
    private Object defaultValue;

    public PropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType, PropertyLocation location, Object defaultValue) {
        this.propertyIndex = propertyIndex;
        this.propertyName = propertyName;
        this.shaderFieldType = shaderFieldType;
        this.location = location;
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

    public PropertyLocation getPropertyLocation() {
        return location;
    }

    public Object getValueToUse(Object givenValue) {
        if (!shaderFieldType.accepts(givenValue))
            return defaultValue;
        else
            return shaderFieldType.convert(givenValue);
    }
}
