package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.LinkedList;
import java.util.List;


public class PropertyBoxImpl extends VisTable implements PropertyBox {
    private String propertyType;
    private List<PropertyBoxPart> propertyBoxParts = new LinkedList<>();
    private VisTextField nameField;

    public PropertyBoxImpl(String name, String propertyType) {
        this.propertyType = propertyType;

        nameField = new VisTextField(name);
        VisTable headerTable = new VisTable();
        headerTable.add(new VisLabel("Name: "));
        headerTable.add(nameField).growX();
        nameField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(true, true));
                    }
                });
        headerTable.row();
        add(headerTable).growX().row();
    }

    @Override
    public String getType() {
        return propertyType;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (PropertyBoxPart graphBoxPart : propertyBoxParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    public void addPropertyBoxPart(PropertyBoxPart propertyBoxPart) {
        propertyBoxParts.add(propertyBoxPart);
        final Actor actor = propertyBoxPart.getActor();
        add(actor).growX().row();
    }

    public void initialize(JsonValue value) {
        if (value != null) {
            for (PropertyBoxPart propertyBoxPart : propertyBoxParts) {
                propertyBoxPart.initialize(value);
            }
        }
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBox createPropertyBox(Skin skin, String id, float x, float y) {
        final String name = getName();
        GraphBoxImpl result = new GraphBoxImpl(id, new PropertyNodeConfiguration(name, propertyType)) {
            @Override
            public JsonValue getData() {
                JsonValue result = new JsonValue(JsonValue.ValueType.object);
                result.addChild("name", new JsonValue(name));
                result.addChild("type", new JsonValue(propertyType));
                return result;
            }
        };
        result.addOutputGraphPart(new ValueGraphNodeOutput(name, propertyType));
        if (ShaderFieldTypeRegistry.findShaderFieldType(propertyType).isTexture()) {
            result.addGraphBoxPart(new TextureSettingsGraphBoxPart());
        }
        return result;
    }

    @Override
    public void dispose() {
        for (PropertyBoxPart propertyBoxPart : propertyBoxParts) {
            propertyBoxPart.dispose();
        }
    }
}
