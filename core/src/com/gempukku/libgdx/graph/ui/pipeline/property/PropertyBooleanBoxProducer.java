package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;


public class PropertyBooleanBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        boolean value = jsonObject.getBoolean("value");
        return createPropertyBoxDefault(name, value);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault("New Boolean", false);
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(String name, boolean value) {
        PropertyBoxImpl<PipelineFieldType> result = new PropertyBoxImpl<>(name, PipelineFieldType.Boolean);
        result.addPropertyBoxPart(new CheckboxBoxPart<PipelineFieldType>("Value", "value", value));

        return result;
    }
}
