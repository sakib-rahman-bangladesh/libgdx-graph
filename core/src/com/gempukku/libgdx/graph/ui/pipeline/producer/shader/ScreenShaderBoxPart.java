package com.gempukku.libgdx.graph.ui.pipeline.producer.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GetSerializedGraph;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.graph.RequestGraphOpen;
import com.gempukku.libgdx.graph.ui.shader.common.UICommonShaderConfiguration;
import com.gempukku.libgdx.graph.ui.shader.screen.UIScreenShaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class ScreenShaderBoxPart extends Table implements GraphBoxPart<PipelineFieldType> {
    private static UIGraphConfiguration<ShaderFieldType>[] graphConfigurations = new UIGraphConfiguration[]{
            new UICommonShaderConfiguration(),
            new UIScreenShaderConfiguration()
    };

    private String id;
    private JsonValue shaderJson;
    private TextField nameTextField;

    public ScreenShaderBoxPart(Skin skin) {
        super(skin);

        this.id = UUID.randomUUID().toString().replace("-", "");
        JsonReader parser = new JsonReader();
        try {
            InputStream is = Gdx.files.classpath("template/empty-screen-shader.json").read();
            try {
                shaderJson = parser.parse(new InputStreamReader(is));
            } finally {
                is.close();
            }
        } catch (IOException exp) {
            // Ignored
        }

        this.nameTextField = new TextField("", skin);
        this.nameTextField.setMessageText("Shader tag");

        final TextButton editButton = new TextButton("Edit", skin);
        editButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        editButton.fire(new RequestGraphOpen(id, "Shader - " + nameTextField.getText(), shaderJson,
                                GraphDesignTab.Type.Full_Screen_Shader, graphConfigurations));
                    }
                });

        add("Tag: ");
        add(nameTextField).growX().row();
        add(editButton).colspan(2).row();
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector<PipelineFieldType> getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector<PipelineFieldType> getInputConnector() {
        return null;
    }

    @Override
    public void dispose() {

    }

    public void initialize(JsonValue data) {
        if (data != null) {
            this.id = data.getString("id");
            this.nameTextField.setText(data.getString("tag"));
            shaderJson = data.get("shader");
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild("id", new JsonValue(id));
        object.addChild("tag", new JsonValue(nameTextField.getText()));
        GetSerializedGraph event = new GetSerializedGraph(id);
        fire(event);
        JsonValue shaderGraph = event.getGraph();
        if (shaderGraph == null)
            shaderGraph = shaderJson;
        object.addChild("shader", shaderGraph);
    }
}
