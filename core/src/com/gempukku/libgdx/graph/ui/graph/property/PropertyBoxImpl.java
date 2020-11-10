package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;


public class PropertyBoxImpl<T extends FieldType> extends Table implements PropertyBox<T> {
    private T propertyType;
    private PropertyDefaultBox propertyDefaultBox;
    private TextField textField;

    public PropertyBoxImpl(Skin skin, String name, T propertyType,
                           PropertyDefaultBox propertyDefaultBox) {
        super(skin);
        this.propertyType = propertyType;
        this.propertyDefaultBox = propertyDefaultBox;

        textField = new TextField(name, skin);
        Table headerTable = new Table(skin);
        headerTable.add(new Label("Name: ", skin));
        headerTable.add(textField).growX();
        textField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(true, true));
                    }
                });
        headerTable.row();
        add(headerTable).growX().row();
        if (propertyDefaultBox != null)
            add(propertyDefaultBox.getActor()).growX().row();
    }

    @Override
    public T getType() {
        return propertyType;
    }

    @Override
    public String getName() {
        return textField.getText();
    }

    @Override
    public JsonValue getData() {
        if (propertyDefaultBox != null) {
            JsonValue data = propertyDefaultBox.serializeData();
            if (data == null)
                return null;
            return data;
        } else {
            return null;
        }
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y) {
        final String name = getName();
        GraphBoxImpl<T> result = new GraphBoxImpl<T>(id, new PropertyNodeConfiguration<T>(name, propertyType), skin) {
            @Override
            public JsonValue getData() {
                JsonValue result = new JsonValue(JsonValue.ValueType.object);
                result.addChild("name", new JsonValue(name));
                result.addChild("type", new JsonValue(propertyType.getName()));
                return result;
            }
        };
        result.addOutputGraphPart(skin, new ValueGraphNodeOutput<T>(name, propertyType));
        if (propertyType.isTexture()) {
            result.addGraphBoxPart(new TextureSettingsGraphBoxPart<T>(skin));
        }
        return result;
    }

    @Override
    public void dispose() {

    }
}
