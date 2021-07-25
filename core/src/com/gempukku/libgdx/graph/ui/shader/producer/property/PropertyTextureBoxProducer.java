package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.FileSelectorBoxPart;

public class PropertyTextureBoxProducer implements PropertyBoxProducer {
    @Override
    public String getType() {
        return "TextureRegion";
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        return createPropertyBoxDefault(skin, name, jsonObject.getString("previewPath", null));
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Texture", null);
    }

    private PropertyBox createPropertyBoxDefault(Skin skin, String name, String previewPath) {
        PropertyBoxImpl result = new PropertyBoxImpl(name, ShaderFieldType.TextureRegion);
        result.addPropertyBoxPart(new FileSelectorBoxPart("Preview texture ", "previewPath", previewPath));

        return result;
    }
}
