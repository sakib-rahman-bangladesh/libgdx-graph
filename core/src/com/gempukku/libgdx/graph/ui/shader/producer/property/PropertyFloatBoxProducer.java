package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;


public class PropertyFloatBoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Float;
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        float x = jsonObject.getFloat("x");
        return createPropertyBoxDefault(skin, name, x);
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Float", (float) 0);
    }

    private PropertyBox<ShaderFieldType> createPropertyBoxDefault(Skin skin, String name, float v1) {
        PropertyBoxImpl<ShaderFieldType> result = new PropertyBoxImpl<>(name, ShaderFieldType.Float);
        result.addPropertyBoxPart(new FloatBoxPart<ShaderFieldType>("X", "x", v1, null));

        return result;
    }
}
