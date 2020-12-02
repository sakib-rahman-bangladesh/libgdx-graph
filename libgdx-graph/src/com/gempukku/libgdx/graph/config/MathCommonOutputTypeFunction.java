package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;

import java.util.function.Function;

public class MathCommonOutputTypeFunction<T extends FieldType> implements Function<ObjectMap<String, Array<T>>, T> {
    private T floatType;
    private String[] types;
    private String[] floatAccepting;

    public MathCommonOutputTypeFunction(T floatType, String[] types, String[] floatAccepting) {
        this.floatType = floatType;
        this.types = types;
        this.floatAccepting = floatAccepting;
    }

    @Override
    public T apply(ObjectMap<String, Array<T>> map) {
        T resolvedType = null;
        for (String input : types) {
            Array<T> type = map.get(input);
            if (type == null || type.size == 0)
                return null;
            if (resolvedType != null && resolvedType != type.get(0))
                return null;
            resolvedType = type.get(0);
        }

        T floatResolvedType = null;
        for (String maybeFloat : floatAccepting) {
            Array<T> type = map.get(maybeFloat);
            if (type == null || type.size == 0)
                return null;
            if (type.get(0) != floatType && type.get(0) != resolvedType)
                return null;
            if (floatResolvedType != null && floatResolvedType != type.get(0))
                return null;
            floatResolvedType = type.get(0);
        }

        return resolvedType;
    }
}
