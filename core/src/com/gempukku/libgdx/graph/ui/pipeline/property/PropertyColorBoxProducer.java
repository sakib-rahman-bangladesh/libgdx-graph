package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;


public class PropertyColorBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Color;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        String color = jsonObject.getString("color");
        return createPropertyBoxDefault(skin, name, color);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Color", "FFFFFFFF");
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name, String colorStr) {
        Color color = Color.valueOf(colorStr);

        PropertyBoxImpl<PipelineFieldType> result = new PropertyBoxImpl<>(name, PipelineFieldType.Color);
        result.addPropertyBoxPart(new ColorBoxPart<PipelineFieldType>("Color", "color", color));

        return result;
    }
}
