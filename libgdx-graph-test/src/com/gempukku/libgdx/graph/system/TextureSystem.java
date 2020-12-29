package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.GameSystem;
import com.gempukku.libgdx.graph.sprite.SpriteProducer;

public class TextureSystem implements GameSystem, SpriteProducer.TextureLoader {
    private ObjectMap<String, Texture> textures = new ObjectMap<>();

    @Override
    public Texture loadTexture(String path) {
        Texture texture = textures.get(path);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(path));
            textures.put(path, texture);
        }
        return texture;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
    }
}
