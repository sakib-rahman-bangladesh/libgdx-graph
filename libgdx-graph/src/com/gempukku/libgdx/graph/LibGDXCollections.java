package com.gempukku.libgdx.graph;

import com.badlogic.gdx.utils.ObjectMap;

public class LibGDXCollections {
    public static <K, V> ObjectMap<K, V> singletonMap(K key, V value) {
        ObjectMap<K, V> result = new ObjectMap<>();
        result.put(key, value);
        return result;
    }

    public static <K, V> ObjectMap<K, V> emptyMap() {
        return new ObjectMap<>();
    }
}
