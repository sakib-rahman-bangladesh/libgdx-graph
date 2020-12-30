package com.gempukku.libgdx.graph.pipeline.loader.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration<PipelineFieldType> getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf(data.getString("type"));
        return new PropertyNodeConfiguration<PipelineFieldType>(name, fieldType);
    }

    @Override
    public PipelineNode createNode(JsonValue data, ObjectMap<String, Array<PipelineNode.FieldOutput<?>>> inputFields) {
        final String propertyName = data.getString("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf(data.getString("type"));

        final PropertyFieldOutput fieldOutput = new PropertyFieldOutput(fieldType, propertyName);

        return new PipelineNode() {
            @Override
            public FieldOutput<?> getFieldOutput(String name) {
                return fieldOutput;
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
        };
    }

    private static class PropertyFieldOutput implements PipelineNode.FieldOutput {
        private PipelineFieldType fieldType;
        private String propertyName;

        public PropertyFieldOutput(PipelineFieldType fieldType, String propertyName) {
            this.fieldType = fieldType;
            this.propertyName = propertyName;
        }

        @Override
        public PipelineFieldType getPropertyType() {
            return fieldType;
        }

        @Override
        public Object getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements) {
            return context.getPipelinePropertySource().getPipelineProperty(propertyName).getValue();
        }
    }
}
