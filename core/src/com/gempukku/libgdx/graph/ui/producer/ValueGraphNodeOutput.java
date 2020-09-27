package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

public class ValueGraphNodeOutput<T extends FieldType> implements GraphNodeOutput<T> {
    private String fieldName;
    private T fieldType;

    public ValueGraphNodeOutput(String fieldName, T fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    @Override
    public boolean isMainConnection() {
        return false;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getFieldId() {
        return "value";
    }

    @Override
    public Array<T> getProducableFieldTypes() {
        Array<T> result = new Array<T>();
        result.add(fieldType);
        return result;
    }

    @Override
    public boolean supportsMultiple() {
        return true;
    }

    @Override
    public T determineFieldType(ObjectMap<String, T> inputs) {
        return fieldType;
    }
}
