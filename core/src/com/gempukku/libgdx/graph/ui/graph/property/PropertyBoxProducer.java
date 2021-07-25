package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;

public interface PropertyBoxProducer {
    String getType();

    PropertyBox createPropertyBox(Skin skin, String name, JsonValue jsonObject);

    PropertyBox createDefaultPropertyBox(Skin skin);
}
