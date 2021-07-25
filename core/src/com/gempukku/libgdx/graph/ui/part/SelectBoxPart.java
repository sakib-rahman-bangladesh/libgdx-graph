package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;


public class SelectBoxPart extends VisTable implements GraphBoxPart {
    private final VisSelectBox<String> selectBox;

    private String property;

    public SelectBoxPart(String label, String property, Enum<?>... values) {
        this(label, property, convertToStrings(values));
    }

    private static String[] convertToStrings(Enum<?>[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].name().replace('_', ' ');
        }
        return result;
    }

    public SelectBoxPart(String label, String property, String... values) {
        this.property = property;

        selectBox = new VisSelectBox<>();
        selectBox.setItems(values);
        add(new VisLabel(label + " "));
        add(selectBox).growX();
        row();

        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(false, true));
                    }
                });
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    public void initialize(JsonValue data) {
        if (data != null) {
            String value = data.getString(property);
            selectBox.setSelected(value);
        }
    }

    public void setSelected(Enum<?> value) {
        selectBox.setSelected(value.name().replace('_', ' '));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(selectBox.getSelected()));
    }

    @Override
    public void dispose() {

    }
}
