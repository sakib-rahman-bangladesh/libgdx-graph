package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.Vector3BoxPart;

public class PropertyVector3BoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public String getType() {
        return "Vector3";
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        PropertyBoxImpl<ShaderFieldType> result = new PropertyBoxImpl<>(name, ShaderFieldType.Vector3);
        result.addPropertyBoxPart(new Vector3BoxPart<ShaderFieldType>("Vector3",
                "x", "y", "z",
                0, 0, 0,
                null, null, null));
        result.initialize(jsonObject);
        return result;
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBox(skin, "New Vector3", null);
    }
}
