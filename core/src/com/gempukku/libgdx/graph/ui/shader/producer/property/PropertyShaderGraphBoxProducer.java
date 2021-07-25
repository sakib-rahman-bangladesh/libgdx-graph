package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.TextureSettingsGraphBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;


public class PropertyShaderGraphBoxProducer implements GraphBoxProducer<ShaderFieldType> {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return "Property";
    }

    @Override
    public String getMenuLocation() {
        return null;
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final String name = data.getString("name");
        final ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(data.getString("type"));
        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, new PropertyNodeConfiguration<>(name, propertyType)) {
            @Override
            public JsonValue getData() {
                JsonValue result = super.getData();
                if (result == null)
                    result = new JsonValue(JsonValue.ValueType.object);
                result.addChild("name", new JsonValue(name));
                result.addChild("type", new JsonValue(propertyType.getName()));
                return result;
            }
        };
        result.addOutputGraphPart(new ValueGraphNodeOutput<>(name, propertyType));
        if (propertyType.isTexture()) {
            TextureSettingsGraphBoxPart<ShaderFieldType> textureSettings = new TextureSettingsGraphBoxPart<>();
            textureSettings.initialize(data);
            result.addGraphBoxPart(textureSettings);
        }

        return result;
    }

    @Override
    public GraphBox<ShaderFieldType> createDefault(Skin skin, String id) {
        return null;
    }
}
