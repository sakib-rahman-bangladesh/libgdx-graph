package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyDefaultBox;


public class PropertyCallbackBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Callback;
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        return createPropertyBoxDefault(skin, name);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Callback");
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name) {
        final Table table = new Table(skin);

        return new PropertyBoxImpl<PipelineFieldType>(skin,
                name,
                PipelineFieldType.Callback,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JsonValue serializeData() {
                        return null;
                    }
                });
    }
}
