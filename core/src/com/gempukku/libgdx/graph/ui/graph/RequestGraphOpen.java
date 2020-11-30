package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;


public class RequestGraphOpen extends Event {
    private String id;
    private String title;
    private JsonValue jsonObject;
    private GraphDesignTab.Type type;
    private UIGraphConfiguration[] graphConfigurations;

    public RequestGraphOpen(String id, String title, JsonValue jsonObject, GraphDesignTab.Type type,
                            UIGraphConfiguration... graphConfiguration) {
        this.id = id;
        this.title = title;
        this.jsonObject = jsonObject;
        this.type = type;
        this.graphConfigurations = graphConfiguration;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public JsonValue getJsonObject() {
        return jsonObject;
    }

    public GraphDesignTab.Type getType() {
        return type;
    }

    public UIGraphConfiguration[] getGraphConfigurations() {
        return graphConfigurations;
    }
}
