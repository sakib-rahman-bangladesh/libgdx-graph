package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.LinkedList;
import java.util.List;


public class PropertyBoxImpl<T extends FieldType> extends VisTable implements PropertyBox<T> {
    private T propertyType;
    private List<PropertyBoxPart<T>> propertyBoxParts = new LinkedList<>();
    private VisTextField nameField;

    public PropertyBoxImpl(String name, T propertyType) {
        super();
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
    public T getType() {
        return propertyType;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (PropertyBoxPart<T> graphBoxPart : propertyBoxParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    public void addPropertyBoxPart(PropertyBoxPart<T> propertyBoxPart) {
        propertyBoxParts.add(propertyBoxPart);
        final Actor actor = propertyBoxPart.getActor();
        add(actor).growX().row();
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y) {
        final String name = getName();
        GraphBoxImpl<T> result = new GraphBoxImpl<T>(id, new PropertyNodeConfiguration<T>(name, propertyType)) {
            @Override
            public JsonValue getData() {
                JsonValue result = new JsonValue(JsonValue.ValueType.object);
                result.addChild("name", new JsonValue(name));
                result.addChild("type", new JsonValue(propertyType.getName()));
                return result;
            }
        };
        result.addOutputGraphPart(new ValueGraphNodeOutput<T>(name, propertyType));
        if (propertyType.isTexture()) {
            result.addGraphBoxPart(new TextureSettingsGraphBoxPart<T>());
        }
        return result;
    }

    @Override
    public void dispose() {
        for (PropertyBoxPart<T> propertyBoxPart : propertyBoxParts) {
            propertyBoxPart.dispose();
        }
    }
}
