package com.gempukku.libgdx.graph.config;

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
        Array<T> inputA = inputs.get(input1);
        Array<T> inputB = inputs.get(input2);
        if (inputA.size < 1 || inputB.size < 1)
            return null;

        T a = inputA.get(0);
        T b = inputB.get(0);

        if (a == floatType)
            return b;
        if (b == floatType)
            return a;
        if (a != b)
            return null;

        return a;
    }
}
