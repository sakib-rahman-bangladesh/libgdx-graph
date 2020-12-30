package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.impl.PipelineRendererImpl;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class PipelineLoaderCallback extends GraphDataLoaderCallback<PipelineRenderer, PipelineFieldType> {
    private TimeProvider timeProvider;

    public PipelineLoaderCallback(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public void start() {

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
            propertyMap.put(property.getName(), findPropertyProducerByType(property.getType()).createProperty(property.getData()));
        }

        return new PipelineRendererImpl(timeProvider,
                pipelineNodeMap.values().toArray(), propertyMap, (EndPipelineNode) pipelineNode);
    }

    @Override
    protected PipelineFieldType getFieldType(String type) {
        return PipelineFieldType.valueOf(type);
    }

    @Override
    protected NodeConfiguration<PipelineFieldType> getNodeConfiguration(String type, JsonValue data) {
        return RendererPipelineConfiguration.pipelineNodeProducers.get(type).getConfiguration(data);
    }

    private PipelinePropertyProducer findPropertyProducerByType(PipelineFieldType type) {
        for (PipelinePropertyProducer pipelinePropertyProducer : RendererPipelineConfiguration.pipelinePropertyProducers) {
            if (pipelinePropertyProducer.getType() == type)
                return pipelinePropertyProducer;
        }
        return null;
    }

    private PipelineNode populatePipelineNodes(String nodeId, ObjectMap<String, PipelineNode> pipelineNodeMap) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        if (pipelineNode != null)
            return pipelineNode;

        GraphNode<PipelineFieldType> nodeInfo = getNodeById(nodeId);
        String nodeInfoType = nodeInfo.getType();
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.pipelineNodeProducers.get(nodeInfoType);
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
