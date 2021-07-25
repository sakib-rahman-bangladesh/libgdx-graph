package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;


public class PropertyVector2BoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector2;
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        float x = jsonObject.getFloat("x");
        float y = jsonObject.getFloat("y");
        return createPropertyBoxDefault(skin, name, x, y);
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Vector2", 0f, 0f);
    }

    private PropertyBox<ShaderFieldType> createPropertyBoxDefault(Skin skin, String name, float v1, float v2) {
        PropertyBoxImpl<ShaderFieldType> result = new PropertyBoxImpl<>(name, ShaderFieldType.Vector2);
        result.addPropertyBoxPart(new FloatBoxPart<ShaderFieldType>("X", "x", v1, null));
        result.addPropertyBoxPart(new FloatBoxPart<ShaderFieldType>("Y", "y", v2, null));

        return result;
    }
}
