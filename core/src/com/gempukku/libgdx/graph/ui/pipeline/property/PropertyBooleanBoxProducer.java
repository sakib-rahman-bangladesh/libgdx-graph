package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyDefaultBox;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;


public class PropertyBooleanBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        boolean value = jsonObject.getBoolean("value");
        return createPropertyBoxDefault(name, value);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault("New Boolean", false);
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(String name, boolean value) {
        final VisCheckBox checkBox = new VisCheckBox("Default");
        checkBox.setChecked(value);
        checkBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        checkBox.fire(new GraphChangedEvent(false, true));
                    }
                });

        final VisTable table = new VisTable();
        table.add(checkBox).grow();

        return new PropertyBoxImpl<PipelineFieldType>(
                name,
                PipelineFieldType.Boolean,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JsonValue serializeData() {
                        JsonValue result = new JsonValue(JsonValue.ValueType.object);
                        result.addChild("value", new JsonValue(checkBox.isChecked()));
                        return result;
                    }
                });
    }
}
