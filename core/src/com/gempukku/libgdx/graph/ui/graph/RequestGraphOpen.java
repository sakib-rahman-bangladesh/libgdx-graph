package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.JsonValue;


public class RequestGraphOpen extends Event {
    private String id;
    private JsonValue jsonObject;

    public RequestGraphOpen(String id, JsonValue jsonObject) {
        this.id = id;
        this.jsonObject = jsonObject;
    }

    public String getId() {
        return id;
    }

    public JsonValue getJsonObject() {
        return jsonObject;
    }
}
