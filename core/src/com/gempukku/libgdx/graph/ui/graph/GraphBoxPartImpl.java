package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;


public class GraphBoxPartImpl<T extends FieldType> implements GraphBoxPart<T> {
    private Actor actor;
    private GraphBoxInputConnector<T> inputConnector;
    private GraphBoxOutputConnector<T> outputConnector;
    private Callback callback;

    public GraphBoxPartImpl(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void setInputConnector(GraphBoxInputConnector.Side side, GraphNodeInput<T> graphNodeInput) {
        inputConnector = new GraphBoxInputConnectorImpl<T>(side, null, graphNodeInput.getFieldId());
    }

    public void setOutputConnector(GraphBoxOutputConnector.Side side, GraphNodeOutput<T> graphNodeOutput) {
        outputConnector = new GraphBoxOutputConnectorImpl<T>(side, null, graphNodeOutput.getFieldId());
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public GraphBoxInputConnector<T> getInputConnector() {
        return inputConnector;
    }

    @Override
    public GraphBoxOutputConnector<T> getOutputConnector() {
        return outputConnector;
    }

    @Override
    public void initialize(JsonValue object) {

    }

    @Override
    public void serializePart(JsonValue object) {
        if (callback != null)
            callback.serialize(object);
    }

    @Override
    public void dispose() {

    }

    public interface Callback {
        void serialize(JsonValue object);
    }
}
