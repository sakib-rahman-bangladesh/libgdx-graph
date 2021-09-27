package com.gempukku.libgdx.graph.plugin.models.design.producer.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.plugin.models.config.material.TextureMaterialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class TextureMaterialBoxProducer extends GraphBoxProducerImpl {
    public TextureMaterialBoxProducer(String type, String name) {
        super(new TextureMaterialShaderNodeConfiguration(type, name));
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        TextureBoxPart normalBoxPart = new TextureBoxPart();
        if (data != null)
            normalBoxPart.initialize(data);
        result.addGraphBoxPart(normalBoxPart);
        return result;
    }

    private static class TextureBoxPart extends VisTable implements GraphBoxPart {
        private VisImage image;
        private Texture texture;
        private String path;
        private TextureRegionDrawable drawable;

        public TextureBoxPart() {
            add(new VisLabel("Preview texture ")).growX();
            VisTextButton chooseFileButton = new VisTextButton("Choose");
            add(chooseFileButton);
            row();
            drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.textureRegion);
            drawable.setMinSize(200, 200);
            image = new VisImage(drawable);
            add(image).colspan(2);
            row();

            chooseFileButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
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
        public GraphBoxOutputConnector getOutputConnector() {
            return null;
        }

        @Override
        public GraphBoxInputConnector getInputConnector() {
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