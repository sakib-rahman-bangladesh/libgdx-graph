package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;


public class PropertyFloatBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Float;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        float x = jsonObject.getFloat("x");
        return createPropertyBoxDefault(skin, name, x);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Float", (float) 0);
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name, float v1) {
        PropertyBoxImpl<PipelineFieldType> result = new PropertyBoxImpl<>(name, PipelineFieldType.Float);
        result.addPropertyBoxPart(new FloatBoxPart<PipelineFieldType>("X", "x", v1, null));

        return result;
    }
}
