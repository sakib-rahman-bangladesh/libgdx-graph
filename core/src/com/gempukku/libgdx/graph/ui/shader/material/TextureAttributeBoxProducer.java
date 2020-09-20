package com.gempukku.libgdx.graph.ui.shader.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.material.TextureAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;


public class TextureAttributeBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public TextureAttributeBoxProducer(String type, String name) {
        super(new TextureAttributeShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBoxImpl<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = super.createPipelineGraphBox(skin, id, data);
        TextureBoxPart normalBoxPart = new TextureBoxPart(skin);
        if (data != null)
            normalBoxPart.initialize(data);
        result.addGraphBoxPart(normalBoxPart);
        return result;
    }

    private static class TextureBoxPart extends Table implements GraphBoxPart<ShaderFieldType> {
        private Image image;
        private Texture texture;
        private String path;
        private TextureRegionDrawable drawable;

        public TextureBoxPart(Skin skin) {
            add(new Label("Preview texture ", skin)).growX();
            TextButton chooseFileButton = new TextButton("Choose", skin);
            add(chooseFileButton);
            row();
            drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.textureRegion);
            drawable.setMinSize(200, 200);
            image = new Image(drawable);
            add(image).colspan(2);
            row();

            chooseFileButton.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                            fileChooser.setModal(true);
                            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                            fileChooser.setListener(new FileChooserAdapter() {
                                @Override
                                public void selected(Array<FileHandle> file) {
                                    attemptToLoadTexture(file.get(0));
                                }
                            });
                            getStage().addActor(fileChooser.fadeIn());
                        }
                    });
        }

        private void attemptToLoadTexture(FileHandle selectedFile) {
            try {
                Texture newTexture = new Texture(selectedFile);
                setTexture(selectedFile.path(), newTexture);
            } catch (Exception exp) {
                // Ignore
            }
        }

        public void initialize(JsonValue data) {
            String previewPath = data.getString("previewPath");
            if (previewPath != null) {
                attemptToLoadTexture(Gdx.files.absolute(previewPath));
            }
        }

        private void setTexture(String path, Texture texture) {
            this.path = path;
            if (this.texture != null) {
                this.texture.dispose();
            }
            this.texture = texture;
            drawable = new TextureRegionDrawable(texture);
            drawable.setMinSize(200, 200);
            image.setDrawable(drawable);
            fire(new GraphChangedEvent(false, true));
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public GraphBoxOutputConnector<ShaderFieldType> getOutputConnector() {
            return null;
        }

        @Override
        public GraphBoxInputConnector<ShaderFieldType> getInputConnector() {
            return null;
        }

        @Override
        public void serializePart(JsonValue object) {
            object.addChild("previewPath", new JsonValue(path));
        }

        @Override
        protected void setStage(Stage stage) {
            super.setStage(stage);
            if (stage == null && texture != null) {
                texture.dispose();
                texture = null;
            } else if (stage != null && path != null) {
                attemptToLoadTexture(Gdx.files.absolute(path));
            }
        }

        @Override
        public void dispose() {
            if (texture != null)
                texture.dispose();
        }
    }
}