package com.gempukku.libgdx.graph.ui.shader.common.producer.property;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyDefaultBox;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;


public class PropertyTextureBoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.TextureRegion;
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JsonValue jsonObject) {
        return createPropertyBoxDefault(skin, name, jsonObject.getString("previewPath", null));
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Texture", null);
    }

    private PropertyBox<ShaderFieldType> createPropertyBoxDefault(Skin skin, String name, String previewPath) {
        final Table table = new Table();
        table.add(new Label("Preview texture ", skin)).growX();
        TextButton chooseFileButton = new TextButton("Choose", skin);
        table.add(chooseFileButton);
        table.row();
        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.textureRegion);
        drawable.setMinSize(200, 200);
        final Image image = new Image(drawable);
        table.add(image).colspan(2);
        table.row();

        final Texture[] textureHolder = new Texture[1];
        final String[] pathHolder = new String[1];

        if (previewPath != null) {
            try {
                Texture texture = new Texture(previewPath);
                pathHolder[0] = previewPath;
                if (textureHolder[0] != null) {
                    textureHolder[0].dispose();
                }
                textureHolder[0] = texture;
                TextureRegionDrawable drawable2 = new TextureRegionDrawable(texture);
                drawable2.setMinSize(200, 200);
                image.setDrawable(drawable2);
            } catch (Exception exp) {
                // Ignore
            }
        }

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
                        table.getStage().addActor(fileChooser.fadeIn());
                    }

                    private void attemptToLoadTexture(FileHandle selectedFile) {
                        try {
                            Texture newTexture = new Texture(selectedFile);
                            setTexture(selectedFile.path(), newTexture);
                        } catch (Exception exp) {
                            // Ignore
                        }
                    }

                    private void setTexture(String path, Texture texture) {
                        pathHolder[0] = path;
                        if (textureHolder[0] != null) {
                            textureHolder[0].dispose();
                        }
                        textureHolder[0] = texture;
                        TextureRegionDrawable drawable = new TextureRegionDrawable(texture);
                        drawable.setMinSize(200, 200);
                        image.setDrawable(drawable);
                        table.fire(new GraphChangedEvent(false, true));
                    }
                });
        return new PropertyBoxImpl<ShaderFieldType>(skin,
                name,
                ShaderFieldType.TextureRegion,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JsonValue serializeData() {
                        if (pathHolder[0] != null) {
                            JsonValue data = new JsonValue(JsonValue.ValueType.object);
                            data.addChild("previewPath", new JsonValue(pathHolder[0]));
                            return data;
                        }
                        return null;
                    }
                }) {
            @Override
            public void dispose() {
                if (textureHolder[0] != null)
                    textureHolder[0].dispose();
            }
        };
    }
}
