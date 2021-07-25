package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;

public class PropertyVector3BoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Vector3;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        float x = jsonObject.getFloat("x");
        float y = jsonObject.getFloat("y");
        float z = jsonObject.getFloat("z");
        return createPropertyBoxDefault(skin, name, x, y, z);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Vector3", 0f, 0f, 0f);
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name, float v1, float v2, float v3) {
        PropertyBoxImpl<PipelineFieldType> result = new PropertyBoxImpl<>(name, PipelineFieldType.Vector3);
        result.addPropertyBoxPart(new FloatBoxPart<PipelineFieldType>("X", "x", v1, null));
        result.addPropertyBoxPart(new FloatBoxPart<PipelineFieldType>("Y", "y", v2, null));
        result.addPropertyBoxPart(new FloatBoxPart<PipelineFieldType>("Z", "z", v3, null));

        return result;
    }
}
