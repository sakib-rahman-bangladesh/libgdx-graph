package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.Array;

public interface GraphNodeInput<T extends FieldType> {
    boolean isRequired();

    boolean isAcceptingMultiple();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    boolean acceptsInputTypes(Array<T> inputTypes);

    Array<T> getAcceptedPropertyTypes();
}
