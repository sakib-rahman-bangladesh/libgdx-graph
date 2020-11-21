package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;


public class CheckboxBoxPart<T extends FieldType> extends Table implements GraphBoxPart<T> {
    private String property;
    private final CheckBox input;

    public CheckboxBoxPart(Skin skin, String label, String property) {
        super(skin);

        this.property = property;

        input = new CheckBox(label, skin);

        add(input).left().grow();
        row();
    }


    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector<T> getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector<T> getInputConnector() {
        return null;
    }

    public void initialize(JsonValue data) {
        input.setChecked(data.getBoolean(property, false));
    }

    public void setValue(boolean value) {
        input.setChecked(value);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.isChecked()));
    }

    @Override
    public void dispose() {

    }
}
