package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.GraphLoaderCallback;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

public class UIGraphLoaderCallback<T extends FieldType> implements GraphLoaderCallback<GraphDesignTab<T>> {
    private Skin skin;
    private GraphDesignTab<T> graphDesignTab;
    private UIGraphConfiguration<T>[] uiGraphConfigurations;

    public UIGraphLoaderCallback(Skin skin, GraphDesignTab<T> graphDesignTab, UIGraphConfiguration<T>... uiGraphConfiguration) {
        this.skin = skin;
        this.graphDesignTab = graphDesignTab;
        this.uiGraphConfigurations = uiGraphConfiguration;
    }

    @Override
    public void start() {
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JsonValue data) {
        GraphBoxProducer<T> producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox<T> graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphDesignTab.getGraphContainer().addGraphBox(graphBox, producer.getName(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphDesignTab.getGraphContainer().addGraphConnection(fromNode, fromProperty, toNode, toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, JsonValue data) {
        PropertyBoxProducer<T> producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox<T> propertyBox = producer.createPropertyBox(skin, name, data);
        graphDesignTab.addPropertyBox(skin, type, propertyBox);
    }

    @Override
    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        graphDesignTab.getGraphContainer().addNodeGroup(name, nodeIds);
    }

    @Override
    public GraphDesignTab<T> end() {
        graphDesignTab.finishedLoading();
        return graphDesignTab;
    }

    private PropertyBoxProducer<T> findPropertyProducerByType(String type) {
        for (UIGraphConfiguration<T> uiGraphConfiguration : uiGraphConfigurations) {
            for (PropertyBoxProducer<T> producer : uiGraphConfiguration.getPropertyBoxProducers().values()) {
                if (producer.getType().getName().equals(type))
                    return producer;
            }
        }

        return null;
    }

    private GraphBoxProducer<T> findProducerByType(String type) {
        for (UIGraphConfiguration<T> uiGraphConfiguration : uiGraphConfigurations) {
            for (GraphBoxProducer<T> graphBoxProducer : uiGraphConfiguration.getGraphBoxProducers()) {
                if (graphBoxProducer.getType().equals(type))
                    return graphBoxProducer;
            }
        }

        return null;
    }
}
