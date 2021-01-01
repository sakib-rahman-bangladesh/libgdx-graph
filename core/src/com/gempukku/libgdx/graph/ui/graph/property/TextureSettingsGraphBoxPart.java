package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TextureSettingsGraphBoxPart<T extends FieldType> extends Table implements GraphBoxPart {
    private static FilterValue[] filterValues = {
            new FilterValue("Default", null),
            new FilterValue("Linear", Texture.TextureFilter.Linear),
            new FilterValue("Nearest", Texture.TextureFilter.Nearest),
            new FilterValue("Linear MipMap Linear", Texture.TextureFilter.MipMapLinearLinear),
            new FilterValue("Nearest MipMap Linear", Texture.TextureFilter.MipMapNearestLinear),
            new FilterValue("Linear MipMap Nearest", Texture.TextureFilter.MipMapLinearNearest),
            new FilterValue("Nearest MipMap Nearest", Texture.TextureFilter.MipMapNearestNearest)
    };
    private static WrapValue[] wrapValues = {
            new WrapValue("Default", null),
            new WrapValue("Clamp to Edge", Texture.TextureWrap.ClampToEdge),
            new WrapValue("Repeat", Texture.TextureWrap.Repeat),
            new WrapValue("Mirrored Repeat", Texture.TextureWrap.MirroredRepeat)
    };

    private TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();

    public TextureSettingsGraphBoxPart(final Skin skin) {
        super(skin);

        final TextButton settingsButton = new TextButton("Texture settings", skin);

        settingsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        SettingsWindow settingsWindow = new SettingsWindow(textureDescriptor, skin);
                        settingsWindow.setSize(250, 260);
                        settingsButton.getStage().addActor(settingsWindow);
                    }
                });

        add(settingsButton).center().row();
    }

    public void initialize(JsonValue data) {
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));
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
        if (textureDescriptor.minFilter != null)
            object.addChild("minFilter", new JsonValue(textureDescriptor.minFilter.toString()));
        if (textureDescriptor.magFilter != null)
            object.addChild("magFilter", new JsonValue(textureDescriptor.magFilter.toString()));
        if (textureDescriptor.uWrap != null)
            object.addChild("uWrap", new JsonValue(textureDescriptor.uWrap.toString()));
        if (textureDescriptor.vWrap != null)
            object.addChild("vWrap", new JsonValue(textureDescriptor.vWrap.toString()));
    }

    @Override
    public void dispose() {

    }

    private static FilterValue findFilter(Texture.TextureFilter textureFilter) {
        for (FilterValue filterValue : filterValues) {
            if (filterValue.getValue() == textureFilter)
                return filterValue;
        }
        return null;
    }

    private static WrapValue findWrap(Texture.TextureWrap textureWrap) {
        for (WrapValue wrapValue : wrapValues) {
            if (wrapValue.getValue() == textureWrap)
                return wrapValue;
        }
        return null;
    }

    private class SettingsWindow extends VisWindow {
        public SettingsWindow(final TextureDescriptor<Texture> textureDescriptor, Skin skin) {
            super("Texture settings");
            addCloseButton();
            setCenterOnAdd(true);
            setModal(true);

            final SelectBox<FilterValue> minFilterBox = new SelectBox<>(skin);
            minFilterBox.setItems(filterValues);
            minFilterBox.setSelected(findFilter(textureDescriptor.minFilter));

            final SelectBox<FilterValue> magFilterBox = new SelectBox<>(skin);
            magFilterBox.setItems(filterValues);
            magFilterBox.setSelected(findFilter(textureDescriptor.magFilter));

            final SelectBox<WrapValue> uWrapBox = new SelectBox<WrapValue>(skin);
            uWrapBox.setItems(wrapValues);
            uWrapBox.setSelected(findWrap(textureDescriptor.uWrap));

            final SelectBox<WrapValue> vWrapBox = new SelectBox<WrapValue>(skin);
            vWrapBox.setItems(wrapValues);
            vWrapBox.setSelected(findWrap(textureDescriptor.vWrap));

            TextButton saveButton = new TextButton("Save", skin);

            add("Minification filtering").left().grow().row();
            add(minFilterBox).left().grow().row();
            add("Magnification filtering").left().grow().row();
            add(magFilterBox).left().grow().row();
            add("U wrapping").left().grow().row();
            add(uWrapBox).left().grow().row();
            add("V wrapping").left().grow().row();
            add(vWrapBox).left().grow().row();

            add(saveButton).center().padTop(5).row();

            saveButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            textureDescriptor.minFilter = minFilterBox.getSelected().getValue();
                            textureDescriptor.magFilter = magFilterBox.getSelected().getValue();
                            textureDescriptor.uWrap = uWrapBox.getSelected().getValue();
                            textureDescriptor.vWrap = vWrapBox.getSelected().getValue();
                            fire(new GraphChangedEvent(false, true));
                            remove();
                        }
                    });
        }
    }

    private static class FilterValue {
        private String name;
        private Texture.TextureFilter value;

        public FilterValue(String name, Texture.TextureFilter value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Texture.TextureFilter getValue() {
            return value;
        }

        public String toString() {
            return name;
        }
    }

    private static class WrapValue {
        private String name;
        private Texture.TextureWrap value;

        public WrapValue(String name, Texture.TextureWrap value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Texture.TextureWrap getValue() {
            return value;
        }

        public String toString() {
            return name;
        }
    }
}
