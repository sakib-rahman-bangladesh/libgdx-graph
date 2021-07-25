package com.gempukku.libgdx.graph.loader;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;

public abstract class GraphDataLoaderCallback<T, U extends FieldType> implements GraphLoaderCallback<T>, Graph<GraphNode, GraphConnection, GraphProperty> {
    private ObjectMap<String, GraphNodeData> graphNodes = new ObjectMap<>();
    private Array<GraphConnectionData> graphConnections = new Array<>();
    private ObjectMap<String, GraphPropertyData> graphProperties = new ObjectMap<>();

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JsonValue data) {
        graphNodes.put(id, new GraphNodeData(id, getNodeConfiguration(type, data), data));
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromField, String toNode, String toField) {
        graphConnections.add(new GraphConnectionData(fromNode, fromField, toNode, toField));
    }

    @Override
    public void addPipelineProperty(String type, String name, JsonValue data) {
        graphProperties.put(name, new GraphPropertyData(name, type, data));
    }

    @Override
    public void addNodeGroup(String name, ObjectSet<String> nodeIds) {
        // Ignore - used only in UI
    }

    @Override
    public GraphNodeData getNodeById(String id) {
        return graphNodes.get(id);
    }

    @Override
    public GraphPropertyData getPropertyByName(String name) {
        return graphProperties.get(name);
    }

    @Override
    public Iterable<? extends GraphConnectionData> getConnections() {
        return graphConnections;
    }

    @Override
    public Iterable<? extends GraphNodeData> getNodes() {
        return graphNodes.values();
    }

    @Override
    public Iterable<? extends GraphPropertyData> getProperties() {
        return graphProperties.values();
    }

    protected abstract NodeConfiguration getNodeConfiguration(String type, JsonValue data);

    private static class GraphNodeData implements GraphNode {
        private String id;
        private NodeConfiguration configuration;
        private JsonValue data;

        public GraphNodeData(String id, NodeConfiguration configuration, JsonValue data) {
            this.id = id;
            this.configuration = configuration;
            this.data = data;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public JsonValue getData() {
            return data;
        }

        @Override
        public NodeConfiguration getConfiguration() {
            return configuration;
        }
    }

    private static class GraphPropertyData implements GraphProperty {
        private String name;
        private String type;
        private JsonValue data;

        public GraphPropertyData(String name, String type, JsonValue data) {
            this.name = name;
            this.type = type;
            this.data = data;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public JsonValue getData() {
            return data;
        }
    }

    private static class GraphConnectionData implements GraphConnection {
        private String nodeFrom;
        private String fieldFrom;
        private String nodeTo;
        private String fieldTo;

        public GraphConnectionData(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
            this.nodeFrom = nodeFrom;
            this.fieldFrom = fieldFrom;
            this.nodeTo = nodeTo;
            this.fieldTo = fieldTo;
        }

        @Override
        public String getNodeFrom() {
            return nodeFrom;
        }

        @Override
        public String getFieldFrom() {
            return fieldFrom;
        }

        @Override
        public String getNodeTo() {
            return nodeTo;
        }

        @Override
        public String getFieldTo() {
            return fieldTo;
        }
    }
}
