package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;

import java.util.function.Function;

public class SameTypeOutputTypeFunction<T extends FieldType> implements Function<ObjectMap<String, Array<T>>, T> {
    private String[] inputs;

    public SameTypeOutputTypeFunction(String... input) {
        this.inputs = input;
    }

    @Override
    public T apply(ObjectMap<String, Array<T>> map) {
        T resolvedType = null;
        for (String input : inputs) {
            Array<T> type = map.get(input);
            if (type == null || type.size == 0)
                return null;
            if (resolvedType != null && resolvedType != type.get(0))
                return null;
            resolvedType = type.get(0);
        }

        return resolvedType;
    }
}
