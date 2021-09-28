package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class AttributeDefinition {
    private String alias;
    private ShaderFieldType fieldType;
    private Object defaultValue;

    public AttributeDefinition(String alias, ShaderFieldType fieldType, Object defaultValue) {
        this.alias = alias;
        this.fieldType = fieldType;
        this.defaultValue = defaultValue;
    }

    public String getAlias() {
        return alias;
    }

    public ShaderFieldType getFieldType() {
        return fieldType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
