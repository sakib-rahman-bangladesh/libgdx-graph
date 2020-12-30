package com.gempukku.libgdx.graph.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.sprite.def.SimpleSpriteDef;
import com.gempukku.libgdx.graph.sprite.def.SpriteDef;
import com.gempukku.libgdx.graph.sprite.def.SpriteStateDataDef;
import com.gempukku.libgdx.graph.sprite.def.StateBasedSpriteDef;
import com.gempukku.libgdx.graph.sprite.def.TiledSpriteDef;

public class SpriteProducer {
    public static Sprite createSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef) {
        String spriteType = spriteDef.getSpriteType();
        switch (spriteType) {
            case "stateBased":
                return createStateBasedSprite(textureLoader, graphSprite, spriteDef, spriteDef.getStateBasedSprite());
            case "tiled":
                return createTiledSprite(textureLoader, graphSprite, spriteDef, spriteDef.getTiledSprite());
            case "simple":
                return createSimpleSprite(textureLoader, graphSprite, spriteDef, spriteDef.getSimpleSprite());
        }
        throw new IllegalArgumentException("Unknown type of sprite");
    }

    private static Sprite createSimpleSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef, SimpleSpriteDef simpleSprite) {
        return new SimpleSprite(graphSprite, spriteDef.getPosition(), spriteDef.getSize(), spriteDef.getAnchor(),
                new TextureRegion(textureLoader.loadTexture(simpleSprite.getTexture()), simpleSprite.getU(), simpleSprite.getV(),
                        simpleSprite.getU2(), simpleSprite.getV2()));
    }

    public static Sprite createStateBasedSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef, StateBasedSpriteDef stateBasedSpriteDef) {
        ObjectMap<String, SpriteStateData> stateData = new ObjectMap<>();
        for (ObjectMap.Entry<String, SpriteStateDataDef> stateEntry : stateBasedSpriteDef.getStateData().entries()) {
            String key = stateEntry.key;
            SpriteStateDataDef def = stateEntry.value;
            SpriteStateData data = new SpriteStateData(
                    new TextureRegion(textureLoader.loadTexture(def.getTexture()), def.getU(), def.getV(), def.getU2(), def.getV2()),
                    def.getWidth(), def.getHeight(), def.getSpeed(), def.isLooping());
            stateData.put(key, data);
        }

        return new StateBasedSprite(graphSprite, spriteDef.getPosition(), spriteDef.getSize(), spriteDef.getAnchor(), stateBasedSpriteDef.getState(), stateBasedSpriteDef.getFaceDirection(), stateData);
    }

    private static Sprite createTiledSprite(TextureLoader textureLoader, GraphSprite graphSprite, SpriteDef spriteDef, TiledSpriteDef tiledSpriteDef) {
        return new TiledSprite(graphSprite, spriteDef.getPosition(), spriteDef.getSize(), spriteDef.getAnchor(),
                new TextureRegion(textureLoader.loadTexture(tiledSpriteDef.getTileTexture()), tiledSpriteDef.getU(), tiledSpriteDef.getV(), tiledSpriteDef.getU2(), tiledSpriteDef.getV2()),
                tiledSpriteDef.getTileRepeat());
    }

    public interface TextureLoader {
        public Texture loadTexture(String path);
    }
}
