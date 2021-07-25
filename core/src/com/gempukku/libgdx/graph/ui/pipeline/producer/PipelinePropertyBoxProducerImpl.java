package com.gempukku.libgdx.graph.ui.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PipelinePropertyBoxProducerImpl implements PropertyBoxProducer<PipelineFieldType> {
    private String defaultName;
    private PipelineFieldType type;
    private List<Supplier<PropertyBoxPart<PipelineFieldType>>> propertyBoxParts = new LinkedList<>();

    public PipelinePropertyBoxProducerImpl(String defaultName, PipelineFieldType type) {
        this.defaultName = defaultName;
        this.type = type;
    }

    public void addPropertyBoxPart(Supplier<PropertyBoxPart<PipelineFieldType>> propertyBoxPart) {
        propertyBoxParts.add(propertyBoxPart);
    }

    @Override
    public String getType() {
        return type.getName();
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        PropertyBoxImpl<PipelineFieldType> result = new PropertyBoxImpl<>(name, type);
        for (Supplier<PropertyBoxPart<PipelineFieldType>> propertyBoxPart : propertyBoxParts) {
            result.addPropertyBoxPart(propertyBoxPart.get());
        }
        result.initialize(jsonObject);

        return result;
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBox(skin, defaultName, null);
    }
}
