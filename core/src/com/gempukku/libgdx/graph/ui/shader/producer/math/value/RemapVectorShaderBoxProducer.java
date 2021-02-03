package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapVectorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class RemapVectorShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public RemapVectorShaderBoxProducer() {
        super(new RemapVectorShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        String x = "X";
        String y = "Y";
        String z = "Z";
        String w = "W";
        if (data != null) {
            x = data.getString("x", x);
            y = data.getString("y", y);
            z = data.getString("z", z);
            w = data.getString("W", w);
        }

        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);

        final SelectBox<String> xBox = createSelectBox(skin, x);
        final SelectBox<String> yBox = createSelectBox(skin, y);
        final SelectBox<String> zBox = createSelectBox(skin, z);
        final SelectBox<String> wBox = createSelectBox(skin, w);

        Table table = new Table(skin);
        table.add("X: ");
        table.add(xBox);
        table.add("Y: ");
        table.add(yBox);
        table.row();
        table.add("Z: ");
        table.add(zBox);
        table.add("W: ");
        table.add(wBox);
        table.row();

        result.addGraphBoxPart(
                new GraphBoxPartImpl<ShaderFieldType>(
                        table,
                        new GraphBoxPartImpl.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                object.addChild("x", new JsonValue(xBox.getSelected()));
                                object.addChild("y", new JsonValue(yBox.getSelected()));
                                object.addChild("z", new JsonValue(zBox.getSelected()));
                                object.addChild("w", new JsonValue(wBox.getSelected()));
                            }
                        }
                ));

        return result;
    }

    private SelectBox<String> createSelectBox(Skin skin, String defaultValue) {
        final SelectBox<String> result = new SelectBox<String>(skin);
        result.setItems("0.0", "1.0", "X", "Y", "Z", "W");
        result.setSelected(defaultValue);
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.fire(new GraphChangedEvent(false, true));
                    }
                });
        return result;
    }

}
