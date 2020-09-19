package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;


public interface GraphBoxPart<T extends FieldType> extends Disposable {
    Actor getActor();

    GraphBoxOutputConnector<T> getOutputConnector();

    GraphBoxInputConnector<T> getInputConnector();

    void serializePart(JsonValue object);
}
