package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;

public interface PropertyBoxProducer<T extends FieldType> {
    String getType();

    PropertyBox<T> createPropertyBox(Skin skin, String name, JsonValue jsonObject);

    PropertyBox<T> createDefaultPropertyBox(Skin skin);
}
