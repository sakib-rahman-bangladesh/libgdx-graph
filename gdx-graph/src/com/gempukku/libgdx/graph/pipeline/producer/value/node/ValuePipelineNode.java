package com.gempukku.libgdx.graph.pipeline.producer.value.node;

import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class ValuePipelineNode implements PipelineNode {
    private NodeConfiguration configuration;
    private String propertyName;
    private Object value;

    public ValuePipelineNode(NodeConfiguration configuration, String propertyName, final Object value) {
        this.configuration = configuration;
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public FieldOutput<?> getFieldOutput(final String name) {
        if (!name.equals(propertyName))
            throw new IllegalArgumentException();
        return new FieldOutput<Object>() {
            @Override
            public String getPropertyType() {
                return configuration.getNodeOutputs().get(name).determineFieldType(null);
            }

            @Override
            public Object getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements) {
                return value;
            }
        };
    }

    @Override
    public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {

    }

    @Override
    public void startFrame() {

    }

    @Override
    public void endFrame() {

    }

    @Override
    public void dispose() {

    }
}
