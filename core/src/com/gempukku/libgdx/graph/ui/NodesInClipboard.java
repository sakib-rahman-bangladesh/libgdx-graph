package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;

public class NodesInClipboard {
    public GraphDesignTab.Type graphType;
    public NodesData[] nodesData;

    class NodesData {
        public String id;
        public String type;
        public float x;
        public float y;
        public JsonValue data;
    }
}
