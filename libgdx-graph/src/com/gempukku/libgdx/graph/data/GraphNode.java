package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphNode<T extends FieldType> {
    String getId();

    JsonValue getData();

    NodeConfiguration<T> getConfiguration();
}
