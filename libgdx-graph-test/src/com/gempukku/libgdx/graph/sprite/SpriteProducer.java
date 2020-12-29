package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.sprite.def.SpriteDef;
import com.gempukku.libgdx.graph.sprite.def.SpriteStateDataDef;

public class SpriteProducer {
    public static Sprite createSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef) {
        String spriteType = spriteDef.getSpriteType();
        switch (spriteType) {
            case "stateBased":
                return createStateBasedSprite(textureLoader, graphSprite, spriteDef);
            case "tiled":
                return createTiledSprite(textureLoader, graphSprite, spriteDef);
        }
        throw new IllegalArgumentException("Unknown type of sprite");
    }

    public static StateBasedSprite createStateBasedSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef) {
        ObjectMap<String, SpriteStateData> stateData = new ObjectMap<>();
        for (ObjectMap.Entry<String, SpriteStateDataDef> stateEntry : spriteDef.getStateData().entries()) {
            String key = stateEntry.key;
            SpriteStateDataDef def = stateEntry.value;
            SpriteStateData data = new SpriteStateData(
                    new TextureRegion(textureLoader.loadTexture(def.getTexture()), def.getU(), def.getV(), def.getU2(), def.getV2()),
                    def.getWidth(), def.getHeight(), def.getSpeed(), def.isLooping());
            stateData.put(key, data);
        }

        return new StateBasedSprite(graphSprite, spriteDef.getPosition(), spriteDef.getSize(), spriteDef.getAnchor(), spriteDef.getState(), spriteDef.getFaceDirection(), stateData);
    }

    private static Sprite createTiledSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef) {
        return new TiledSprite(graphSprite, spriteDef.getPosition(), spriteDef.getSize(), spriteDef.getAnchor(),
                new TextureRegion(textureLoader.loadTexture(spriteDef.getTileTexture()), spriteDef.getU(), spriteDef.getV(), spriteDef.getU2(), spriteDef.getV2()),
                spriteDef.getTileRepeat());
    }

    public interface TextureLoader {
        public Texture loadTexture(String path);
    }
}
