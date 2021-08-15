package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.Separator;

public class SeparatorBoxPart implements GraphBoxPart {
    private Separator separator = new Separator();

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    @Override
    public Actor getActor() {
        return separator;
    }

    @Override
    public void initialize(JsonValue object) {

    }

    @Override
    public void serializePart(JsonValue object) {

    }

    @Override
    public void dispose() {

    }
}
