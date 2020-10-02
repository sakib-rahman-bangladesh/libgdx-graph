package com.gempukku.libgdx.graph.ui.pipeline.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GetSerializedGraph;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphRemoved;
import com.gempukku.libgdx.graph.ui.graph.RequestGraphOpen;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.Separator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GraphShadersBoxPart extends Table implements GraphBoxPart<PipelineFieldType> {
    private static final int EDIT_WIDTH = 50;
    private static final int REMOVE_WIDTH = 80;
    private final VerticalGroup renderPasses;
    private final Skin skin;
    private List<RenderPassInfo> passes = new LinkedList<>();

    public GraphShadersBoxPart(Skin skin) {
        this.skin = skin;

        renderPasses = new VerticalGroup();
        renderPasses.top();
        renderPasses.grow();

        Table table = new Table(skin);
        table.add("Tag").growX();
        table.add("Edit").width(EDIT_WIDTH);
        table.add("Remove").width(REMOVE_WIDTH);
        table.row();
        renderPasses.addActor(table);

        ScrollPane scrollPane = new ScrollPane(renderPasses);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        add(scrollPane).grow().row();

        TextButton newRenderPass = new TextButton("New Render Pass", skin);
        newRenderPass.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        addRenderPass();
                    }
                });

        Table buttonTable = new Table(skin);
        buttonTable.add(newRenderPass);

        add(buttonTable).growX().row();
    }

    public void initialize(JsonValue data) {
        JsonValue renderPasses = data.get("renderPasses");
        for (JsonValue renderPass : renderPasses) {
            RenderPassInfo renderPassInfo = addRenderPass();
            renderPassInfo.initialize(renderPass);
        }
    }

    @Override
    public float getPrefWidth() {
        return 300;
    }

    @Override
    public float getPrefHeight() {
        return 200;
    }

    private RenderPassInfo addRenderPass() {
        RenderPassInfo renderPassInfo = new RenderPassInfo();
        passes.add(renderPassInfo);
        renderPasses.addActor(renderPassInfo.getActor());
        return renderPassInfo;
    }

    private void removeRenderPass(RenderPassInfo renderPassInfo) {
        passes.remove(renderPassInfo);
        renderPasses.removeActor(renderPassInfo.getActor());
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
    public void serializePart(JsonValue object) {
        JsonValue passArray = new JsonValue(JsonValue.ValueType.array);
        for (RenderPassInfo pass : passes) {
            JsonValue passObject = new JsonValue(JsonValue.ValueType.object);
            pass.serializePass(passObject);
            passArray.addChild(passObject);
        }
        object.addChild("renderPasses", passArray);
    }

    @Override
    public void dispose() {

    }

    private class RenderPassInfo {
        private Table table;
        private VerticalGroup shaderGroup = new VerticalGroup();
        private List<ShaderInfo> shaders = new LinkedList<>();

        public RenderPassInfo() {
            table = new Table();
            table.add(new Separator()).growX().row();
            table.add(shaderGroup).growX().row();

            TextButton newShader = new TextButton("New Shader", skin);
            newShader.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String id = UUID.randomUUID().toString().replace("-", "");
                            try {
                                JsonReader parser = new JsonReader();
                                InputStream is = Gdx.files.classpath("template/empty-shader.json").read();
                                try {
                                    JsonValue shader = parser.parse(new InputStreamReader(is));
                                    addShaderGraph(id, "", shader);
                                } finally {
                                    is.close();
                                }
                            } catch (IOException exp) {

                            }
                        }
                    });

            TextButton removePass = new TextButton("Remove Pass", skin);
            removePass.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Dialogs.showOptionDialog(getStage(), "Confirm", "Would you like to remove the render pass?",
                                    Dialogs.OptionDialogType.YES_CANCEL, new OptionDialogListener() {
                                        @Override
                                        public void yes() {
                                            for (ShaderInfo shader : shaders) {
                                                fire(new GraphRemoved(shader.getId()));
                                            }
                                            removeRenderPass(RenderPassInfo.this);
                                        }

                                        @Override
                                        public void no() {

                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                        }
                    });

            Table buttons = new Table(skin);
            buttons.add(newShader);
            buttons.add(removePass);
            table.add(buttons).growX().row();
        }

        private void addShaderGraph(String id, String tag, JsonValue shader) {
            ShaderInfo shaderInfo = new ShaderInfo(this, id, tag, shader);
            shaders.add(shaderInfo);
            shaderGroup.addActor(shaderInfo.getActor());
        }

        private void removeShaderGraph(ShaderInfo shaderInfo) {
            shaders.remove(shaderInfo);
            shaderGroup.removeActor(shaderInfo.getActor());
        }

        public void initialize(JsonValue data) {
            JsonValue shaderArray = data.get("shaders");
            for (JsonValue shaderObject : shaderArray) {
                String id = shaderObject.getString("id");
                String tag = shaderObject.getString("tag");
                JsonValue shader = shaderObject.get("shader");
                addShaderGraph(id, tag, shader);
            }
        }

        public void serializePass(JsonValue object) {
            JsonValue shaderArray = new JsonValue(JsonValue.ValueType.array);
            for (ShaderInfo shader : shaders) {
                JsonValue shaderObject = new JsonValue(JsonValue.ValueType.object);
                shaderObject.addChild("id", new JsonValue(shader.getId()));
                shaderObject.addChild("tag", new JsonValue(shader.getTag()));
                GetSerializedGraph event = new GetSerializedGraph(shader.getId());
                fire(event);
                JsonValue shaderGraph = event.getGraph();
                if (shaderGraph == null)
                    shaderGraph = shader.getInitialShaderJson();
                shaderObject.addChild("shader", shaderGraph);
                shaderArray.addChild(shaderObject);
            }

            object.addChild("shaders", shaderArray);
        }

        public Actor getActor() {
            return table;
        }
    }

    private class ShaderInfo {
        private String id;
        private JsonValue initialShaderJson;
        private Table table;
        private TextField textField;

        public ShaderInfo(final RenderPassInfo renderPass, final String id, String tag, final JsonValue initialShaderJson) {
            this.id = id;
            this.initialShaderJson = initialShaderJson;
            table = new Table(skin);
            textField = new TextField(tag, skin);
            textField.setMessageText("Shader Tag");
            table.add(textField).growX();
            final TextButton editButton = new TextButton("Edit", skin);
            editButton.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            editButton.fire(new RequestGraphOpen(id, initialShaderJson));
                        }
                    });
            table.add(editButton).width(EDIT_WIDTH);
            final TextButton deleteButton = new TextButton("Remove", skin);
            deleteButton.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Dialogs.showOptionDialog(getStage(), "Confirm", "Would you like to remove the shader?",
                                    Dialogs.OptionDialogType.YES_CANCEL, new OptionDialogListener() {
                                        @Override
                                        public void yes() {
                                            fire(new GraphRemoved(id));
                                            renderPass.removeShaderGraph(ShaderInfo.this);
                                        }

                                        @Override
                                        public void no() {

                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                        }
                    });
            table.add(deleteButton).width(REMOVE_WIDTH);
            table.row();
        }

        public String getId() {
            return id;
        }

        public Table getActor() {
            return table;
        }

        public String getTag() {
            return textField.getText();
        }

        public JsonValue getInitialShaderJson() {
            return initialShaderJson;
        }
    }
}
