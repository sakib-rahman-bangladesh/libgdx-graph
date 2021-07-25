package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector2BoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector3BoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyBoxProducerImpl;

import java.util.function.Supplier;

public class UIPipelineConfigurer {
    public static void processPipelineConfig(JsonValue value) {
        JsonValue propertyTypes = value.get("propertyTypes");
        if (propertyTypes != null) {
            for (JsonValue propertyType : propertyTypes) {
                processPropertyType(propertyType);
            }
        }
    }

    private static void processPropertyType(JsonValue propertyType) {
        final String typeName = propertyType.name();
        final String defaultName = propertyType.getString("defaultName");
        final boolean texture = propertyType.getBoolean("texture", false);
        PipelineFieldType pipelineFieldType = new PipelineFieldType() {
            @Override
            public boolean accepts(Object value) {
                return false;
            }

            @Override
            public Object convert(Object value) {
                return null;
            }

            @Override
            public String getName() {
                return typeName;
            }

            @Override
            public boolean isTexture() {
                return texture;
            }
        };
        PipelinePropertyBoxProducerImpl producer = new PipelinePropertyBoxProducerImpl(defaultName, pipelineFieldType);

        JsonValue fields = propertyType.get("fields");
        if (fields != null) {
            for (final JsonValue field : fields) {
                final String fieldType = field.getString("type");
                producer.addPropertyBoxPart(
                        new Supplier<PropertyBoxPart<PipelineFieldType>>() {
                            @Override
                            public PropertyBoxPart<PipelineFieldType> get() {
                                switch (fieldType) {
                                    case "Float": {
                                        return new FloatBoxPart<>(field.getString("label") + " ", field.getString("property"),
                                                field.getFloat("defaultValue", 0), null);
                                    }
                                    case "Vector2": {
                                        JsonValue properties = field.get("properties");
                                        JsonValue defaultValues = field.get("defaultValues");
                                        return new Vector2BoxPart<>(field.getString("label") + " ",
                                                properties.getString(0), properties.getString(1),
                                                defaultValues.getFloat(0), defaultValues.getFloat(1),
                                                null, null);
                                    }
                                    case "Vector3": {
                                        JsonValue properties = field.get("properties");
                                        JsonValue defaultValues = field.get("defaultValues");
                                        return new Vector3BoxPart<>(field.getString("label") + " ",
                                                properties.getString(0), properties.getString(1), properties.getString(2),
                                                defaultValues.getFloat(0), defaultValues.getFloat(1), defaultValues.getFloat(2),
                                                null, null, null);
                                    }
                                    case "Color": {
                                        return new ColorBoxPart<>(field.getString("label") + " ", field.getString("property"),
                                                Color.valueOf(field.getString("defaultValue")));
                                    }
                                    case "Boolean": {
                                        return new CheckboxBoxPart<>(field.getString("label") + " ", field.getString("property"),
                                                field.getBoolean("defaultValue"));
                                    }
                                }
                                return null;
                            }
                        }
                );
            }
        }

        UIPipelineConfiguration.registerPropertyType(producer);
    }
}
