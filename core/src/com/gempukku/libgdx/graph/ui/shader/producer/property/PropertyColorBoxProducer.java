package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;


public class PropertyColorBoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public String getType() {
        return "Vector4";
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        String color = jsonObject.getString("color");
        return createPropertyBoxDefault(skin, name, color);
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Color", "FFFFFFFF");
    }

    private PropertyBox<ShaderFieldType> createPropertyBoxDefault(Skin skin, String name, String colorStr) {
        Color color = Color.valueOf(colorStr);

        PropertyBoxImpl<ShaderFieldType> result = new PropertyBoxImpl<>(name, ShaderFieldType.Vector4);
        result.addPropertyBoxPart(new ColorBoxPart<ShaderFieldType>("Color", "color", color));

        return result;
    }
}
