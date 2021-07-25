package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.impl.PipelineRendererImpl;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class PipelineLoaderCallback extends GraphDataLoaderCallback<PipelineRenderer, PipelineFieldType> {
    private TimeProvider timeProvider;
    private PluginRegistryImpl pluginRegistry;
    private PipelineRendererResources resources;

    public PipelineLoaderCallback(TimeProvider timeProvider) {
        this(timeProvider, null);
    }

    public PipelineLoaderCallback(TimeProvider timeProvider, PipelineRendererResources resources) {
        this.timeProvider = timeProvider;
        this.resources = resources;
    }

    @Override
    public void start() {
        try {
            pluginRegistry = PluginRegistryImpl.initializePlugins();
        } catch (ReflectionException e) {
            throw new GdxRuntimeException(e);
        }
    }

    @Override
    public PipelineRenderer end() {
        GraphValidator<GraphNode<PipelineFieldType>, GraphConnection, GraphProperty<PipelineFieldType>, PipelineFieldType> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode<PipelineFieldType>, GraphConnection, GraphProperty<PipelineFieldType>, PipelineFieldType> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        ObjectMap<String, PipelineNode> pipelineNodeMap = new ObjectMap<>();
        PipelineNode pipelineNode = populatePipelineNodes("end", pipelineNodeMap);

        ObjectMap<String, WritablePipelineProperty> propertyMap = new ObjectMap<>();
        for (GraphProperty<PipelineFieldType> property : getProperties()) {
            propertyMap.put(property.getName(), RendererPipelineConfiguration.findProperty(property.getType()).createProperty(property.getData()));
        }

        return new PipelineRendererImpl(pluginRegistry, timeProvider,
                pipelineNodeMap.values().toArray(), propertyMap, (EndPipelineNode) pipelineNode, resources);
    }

    @Override
    protected PipelineFieldType getFieldType(String type) {
        return PipelineFieldTypeRegistry.findPipelineFieldType(type);
    }

    @Override
    protected NodeConfiguration<PipelineFieldType> getNodeConfiguration(String type, JsonValue data) {
        return RendererPipelineConfiguration.findProducer(type).getConfiguration(data);
    }

    private PipelineNode populatePipelineNodes(String nodeId, ObjectMap<String, PipelineNode> pipelineNodeMap) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        if (pipelineNode != null)
            return pipelineNode;

        GraphNode<PipelineFieldType> nodeInfo = getNodeById(nodeId);
        String nodeInfoType = nodeInfo.getConfiguration().getType();
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.findProducer(nodeInfoType);
        if (nodeProducer == null)
            throw new IllegalStateException("Unable to find node producer for type: " + nodeInfoType);
        ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputFields = new ObjectMap<>();
        for (GraphNodeInput<PipelineFieldType> nodeInput : new ObjectMap.Values<>(nodeProducer.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
            String inputName = nodeInput.getFieldId();
            Array<GraphConnection> vertexInfos = findInputProducers(nodeId, inputName);
            if (vertexInfos.size == 0 && nodeInput.isRequired())
                throw new IllegalStateException("Required input not provided");

            Array<PipelineFieldType> fieldTypes = new Array<>();
            Array<PipelineNode.FieldOutput<?>> fieldOutputs = new Array<>();
            for (GraphConnection vertexInfo : vertexInfos) {
                PipelineNode vertexNode = populatePipelineNodes(vertexInfo.getNodeFrom(), pipelineNodeMap);
                PipelineNode.FieldOutput<?> fieldOutput = vertexNode.getFieldOutput(vertexInfo.getFieldFrom());
                PipelineFieldType fieldType = fieldOutput.getPropertyType();
                fieldTypes.add(fieldType);
                fieldOutputs.add(fieldOutput);
            }
            if (!nodeInput.acceptsInputTypes(fieldTypes))
                throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
            inputFields.put(inputName, fieldOutputs);
        }
        pipelineNode = nodeProducer.createNode(nodeInfo.getData(), inputFields);
        pipelineNodeMap.put(nodeId, pipelineNode);
        return pipelineNode;
    }

    private Array<GraphConnection> findInputProducers(String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }
}
