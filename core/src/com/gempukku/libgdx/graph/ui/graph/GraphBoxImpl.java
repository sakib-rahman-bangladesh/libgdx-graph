package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GraphBoxImpl<T extends FieldType> implements GraphBox<T> {
    private String id;
    private NodeConfiguration<T> configuration;
    private VisTable table;
    private List<GraphBoxPart<T>> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxInputConnector<T>> inputConnectors = new HashMap<>();
    private Map<String, GraphBoxOutputConnector<T>> outputConnectors = new HashMap<>();

    public GraphBoxImpl(String id, NodeConfiguration<T> configuration) {
        this.id = id;
        this.configuration = configuration;
        table = new VisTable();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<T>, ? extends GraphConnection, ? extends GraphProperty<T>, T> graph) {

    }

    @Override
    public NodeConfiguration<T> getConfiguration() {
        return configuration;
    }

    public void addTopConnector(GraphNodeInput<T> graphNodeInput) {
        inputConnectors.put(graphNodeInput.getFieldId(), new GraphBoxInputConnectorImpl<T>(GraphBoxInputConnector.Side.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, graphNodeInput.getFieldId()));
    }

    public void addBottomConnector(GraphNodeOutput<T> graphNodeOutput) {
        outputConnectors.put(graphNodeOutput.getFieldId(), new GraphBoxOutputConnectorImpl<T>(GraphBoxOutputConnector.Side.Bottom,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return table.getWidth() / 2f;
                    }
                }, graphNodeOutput.getFieldId()));
    }

    public void addTwoSideGraphPart(GraphNodeInput<T> graphNodeInput,
                                    GraphNodeOutput<T> graphNodeOutput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName())).grow();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName());
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        GraphBoxPartImpl<T> graphBoxPart = new GraphBoxPartImpl<T>(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, graphNodeInput);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(GraphNodeInput<T> graphNodeInput) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName())).grow().row();

        GraphBoxPartImpl<T> graphBoxPart = new GraphBoxPartImpl<T>(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, graphNodeInput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            GraphNodeOutput<T> graphNodeOutput) {
        VisTable table = new VisTable();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName());
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        GraphBoxPartImpl<T> graphBoxPart = new GraphBoxPartImpl<T>(table, null);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, graphNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphBoxPart<T> graphBoxPart) {
        graphBoxParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
        final GraphBoxInputConnector<T> inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new GraphBoxInputConnectorImpl<T>(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getFieldId()));
        }
        final GraphBoxOutputConnector<T> outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new GraphBoxOutputConnectorImpl<T>(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector.getFieldId()));
        }
    }

    @Override
    public Map<String, GraphBoxInputConnector<T>> getInputs() {
        return inputConnectors;
    }

    @Override
    public Map<String, GraphBoxOutputConnector<T>> getOutputs() {
        return outputConnectors;
    }

    @Override
    public Actor getActor() {
        return table;
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphBoxPart<T> graphBoxPart : graphBoxParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    @Override
    public void dispose() {
        for (GraphBoxPart<T> part : graphBoxParts) {
            part.dispose();
        }
    }
}
