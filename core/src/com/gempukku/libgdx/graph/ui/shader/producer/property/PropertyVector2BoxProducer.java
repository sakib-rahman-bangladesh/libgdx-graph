package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.Vector2BoxPart;

public class PropertyVector2BoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return "Vector2";
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, ShaderFieldType.Vector2);
        result.addPropertyBoxPart(new Vector2BoxPart("Vector2", "x", "y", 0, 0, null, null));
        result.initialize(jsonObject);
        return result;
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBox(skin, "New Vector2", null);
    }
}
