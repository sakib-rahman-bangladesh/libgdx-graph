package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.widget.VisTextField;


public class StringBoxPart<T extends FieldType> extends Table implements GraphBoxPart<T> {
    private String property;
    private final VisTextField input;

    public StringBoxPart(Skin skin, String label, String property) {
        super(skin);

        this.property = property;
        input = new VisTextField();
        input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        input.fire(new GraphChangedEvent(false, true));
                    }
                });

        add(new Label(label, skin));
        add(input).growX();
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
        String value = data.getString(property);
        input.setText(value);
    }

    public void setValue(String value) {
        input.setText(value);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.getText()));
    }

    @Override
    public void dispose() {

    }
}
