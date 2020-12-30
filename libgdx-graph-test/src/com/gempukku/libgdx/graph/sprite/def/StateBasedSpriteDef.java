package com.gempukku.libgdx.graph.sprite.def;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;

public class StateBasedSpriteDef {
    // State based attributes
    private SpriteFaceDirection faceDirection;
    private String state;
    private ObjectMap<String, SpriteStateDataDef> stateData;

    public SpriteFaceDirection getFaceDirection() {
        return faceDirection;
    }

    public String getState() {
        return state;
    }

    public ObjectMap<String, SpriteStateDataDef> getStateData() {
        return stateData;
    }
}
