package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.part.ColorBoxPart;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector2BoxPart;
import com.gempukku.libgdx.graph.ui.part.Vector3BoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class UIPipelineConfigurer {
    public static void processPipelineConfig(JsonValue value) {
        JsonValue propertyTypes = value.get("propertyTypes");
        if (propertyTypes != null) {
            for (JsonValue propertyType : propertyTypes) {
                processPropertyType(propertyType);
            }
        }
//        JsonValue boxProducers = value.get("boxProducers");
//        if (boxProducers != null) {
//            for (JsonValue boxProducer : boxProducers) {
//                processBoxProducer(boxProducer);
//            }
//        }
    }

    private static void processBoxProducer(JsonValue boxProducer) {
        String producerType = boxProducer.name();
        String producerName = boxProducer.getString("name");
        String menuLocation = boxProducer.getString("menuLocation");

        NodeConfigurationImpl nodeConfiguration = new NodeConfigurationImpl(producerType, producerName, menuLocation);
        JsonValue inputs = boxProducer.get("inputs");
        if (inputs != null) {
            for (JsonValue input : inputs) {

            }
        }
        JsonValue outputs = boxProducer.get("outputs");
        if (outputs != null) {
            for (JsonValue output : outputs) {
                String id = output.getString("id");
                String name = output.getString("name");
                boolean mainConnection = output.getBoolean("mainConnection", false);
                JsonValue type = output.get("type");
                JsonValue validation = output.get("validation");
                Function<ObjectMap<String, Array<String>>, String> validationFunction = convertToValidationFunction(validation);
                String[] types = convertToArrayOfStrings(type);
                GraphNodeOutputImpl nodeOutput = new GraphNodeOutputImpl(id, name, mainConnection,
                        validationFunction, types);
                nodeConfiguration.addNodeOutput(nodeOutput);
            }
        }
        GraphBoxProducerImpl producer = new GraphBoxProducerImpl(nodeConfiguration);
        UIPipelineConfiguration.register(producer);
    }

    private static Function<ObjectMap<String, Array<String>>, String> convertToValidationFunction(JsonValue validation) {
        return null;
    }

    private static String[] convertToArrayOfStrings(JsonValue type) {
        if (type.isArray()) {
            List<String> result = new ArrayList<>();
            for (JsonValue jsonValue : type) {
                result.add(jsonValue.asString());
            }
            return result.toArray(new String[0]);
        } else {
            return new String[]{type.asString()};
        }
    }

    private static void processPropertyType(JsonValue propertyType) {
        final String typeName = propertyType.name();
        final String defaultName = propertyType.getString("defaultName");
        PipelinePropertyBoxProducerImpl producer = new PipelinePropertyBoxProducerImpl(defaultName, typeName);

        JsonValue fields = propertyType.get("fields");
        if (fields != null) {
            for (final JsonValue field : fields) {
                final String fieldType = field.getString("type");
                producer.addPropertyBoxPart(
                        new Supplier<PropertyBoxPart>() {
                            @Override
                            public PropertyBoxPart get() {
                                switch (fieldType) {
                                    case "Float": {
                                        return new FloatBoxPart(field.getString("label") + " ", field.getString("property"),
                                                field.getFloat("defaultValue", 0), null);
                                    }
                                    case "Vector2": {
                                        JsonValue properties = field.get("properties");
                                        JsonValue defaultValues = field.get("defaultValues");
                                        return new Vector2BoxPart(field.getString("label") + " ",
                                                properties.getString(0), properties.getString(1),
                                                defaultValues.getFloat(0), defaultValues.getFloat(1),
                                                null, null);
                                    }
                                    case "Vector3": {
                                        JsonValue properties = field.get("properties");
                                        JsonValue defaultValues = field.get("defaultValues");
                                        return new Vector3BoxPart(field.getString("label") + " ",
                                                properties.getString(0), properties.getString(1), properties.getString(2),
                                                defaultValues.getFloat(0), defaultValues.getFloat(1), defaultValues.getFloat(2),
                                                null, null, null);
                                    }
                                    case "Color": {
                                        return new ColorBoxPart(field.getString("label") + " ", field.getString("property"),
                                                Color.valueOf(field.getString("defaultValue")));
                                    }
                                    case "Boolean": {
                                        return new CheckboxBoxPart(field.getString("label") + " ", field.getString("property"),
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
