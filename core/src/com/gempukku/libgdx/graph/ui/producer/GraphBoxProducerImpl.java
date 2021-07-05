package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;

import java.util.Iterator;

public class GraphBoxProducerImpl<T extends FieldType> implements GraphBoxProducer<T> {
    private NodeConfiguration<T> configuration;

    public GraphBoxProducerImpl(NodeConfiguration<T> configuration) {
        this.configuration = configuration;
    }

    public NodeConfiguration<T> getConfiguration() {
        return configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public GraphBox<T> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<T> start = createGraphBox(id);
        addConfigurationInputsAndOutputs(start);

        return start;
    }

    protected GraphBoxImpl<T> createGraphBox(String id) {
        return new GraphBoxImpl<T>(id, configuration);
    }

    protected void addConfigurationInputsAndOutputs(GraphBoxImpl<T> graphBox) {
        Iterator<GraphNodeInput<T>> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput<T>> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput<T> input = null;
            GraphNodeOutput<T> output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    graphBox.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    graphBox.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                graphBox.addTwoSideGraphPart(input, output);
            } else if (input != null) {
                graphBox.addInputGraphPart(input);
            } else if (output != null) {
                graphBox.addOutputGraphPart(output);
            }
        }
    }

    @Override
    public GraphBox<T> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }
}
