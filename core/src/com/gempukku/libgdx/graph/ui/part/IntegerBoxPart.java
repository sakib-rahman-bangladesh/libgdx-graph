package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;


public class IntegerBoxPart<T extends FieldType> extends Table implements GraphBoxPart<T> {
    private String property;
    private final VisValidatableTextField v1Input;

    public IntegerBoxPart(Skin skin, String label, String property, int defaultValue, InputValidator inputValidator) {
        super(skin);

        this.property = property;
        v1Input = new VisValidatableTextField(Validators.INTEGERS, inputValidator) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(defaultValue));
        v1Input.setAlignment(Align.right);
        v1Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v1Input.fire(new GraphChangedEvent(false, true));
                    }
                });

        add(new Label(label, skin));
        add(v1Input).growX();
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
        int value = data.getInt(property);
        v1Input.setText(String.valueOf(value));
    }

    public void setValue(float value) {
        v1Input.setText(String.valueOf(value));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(Integer.parseInt(v1Input.getText())));
    }

    @Override
    public void dispose() {

    }
}
