package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;

import java.util.function.Function;

public class MultiParamVectorArithmeticOutputTypeFunction<T extends FieldType> implements Function<ObjectMap<String, Array<T>>, T> {
    private T floatType;
    private String input;

    public MultiParamVectorArithmeticOutputTypeFunction(T floatType, String input) {
        this.floatType = floatType;
        this.input = input;
    }

    @Override
    public T apply(ObjectMap<String, Array<T>> inputs) {
        Array<T> types = inputs.get(input);
        if (types.size == 0)
            return null;

        T resultType = floatType;
        for (T type : types) {
            if (type != resultType && (resultType != floatType && type != floatType))
                return null;
            if (type != floatType)
                resultType = type;
        }

        return resultType;
    }
}
