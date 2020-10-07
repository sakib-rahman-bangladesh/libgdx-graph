package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.FieldType;

import java.util.function.Function;

public class VectorArithmeticOutputTypeFunction<T extends FieldType> implements Function<ObjectMap<String, Array<T>>, T> {
    private T floatType;
    private String input1;
    private String input2;

    public VectorArithmeticOutputTypeFunction(T floatType, String input1, String input2) {
        this.floatType = floatType;
        this.input1 = input1;
        this.input2 = input2;
    }

    @Override
    public T apply(ObjectMap<String, Array<T>> inputs) {
        T a = inputs.get(input1).get(0);
        T b = inputs.get(input2).get(0);
        if (a == null || b == null)
            return null;

        if (a == floatType)
            return b;
        if (b == floatType)
            return a;
        if (a != b)
            return null;

        return a;
    }
}
