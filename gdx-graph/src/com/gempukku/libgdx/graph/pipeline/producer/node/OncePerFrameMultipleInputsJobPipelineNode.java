package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;

public abstract class OncePerFrameMultipleInputsJobPipelineNode implements PipelineNode {
    private boolean executedInFrame;
    private NodeConfiguration configuration;
    private ObjectMap<String, Array<FieldOutput<?>>> inputFields;
    private ObjectMap<String, WorkerFieldOutput<Object>> workerFieldOutputs = new ObjectMap<>();

    public OncePerFrameMultipleInputsJobPipelineNode(NodeConfiguration configuration, ObjectMap<String, Array<FieldOutput<?>>> inputFields) {
        this.configuration = configuration;
        this.inputFields = inputFields;
    }

    @Override
    public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {

    }

    @Override
    public FieldOutput<?> getFieldOutput(String name) {
        WorkerFieldOutput<Object> fieldOutput = workerFieldOutputs.get(name);
        if (fieldOutput == null) {
            String fieldType = determineOutputType(name, inputFields);
            fieldOutput = new WorkerFieldOutput<>(fieldType);
            workerFieldOutputs.put(name, fieldOutput);
        }

        return fieldOutput;
    }

    private String determineOutputType(String name, ObjectMap<String, Array<FieldOutput<?>>> inputFields) {
        ObjectMap<String, Array<String>> inputs = new ObjectMap<>();
        for (String field : inputFields.keys()) {
            Array<String> types = new Array<>();
            for (FieldOutput<?> fieldOutput : inputFields.get(field)) {
                types.add(fieldOutput.getPropertyType());
            }
            inputs.put(field, types);
        }

        return configuration.getNodeOutputs().get(name).determineFieldType(inputs);
    }

    protected abstract void executeJob(PipelineRenderingContext pipelineRenderingContext, ObjectMap<String, ? extends OutputValue> outputValues);

    @Override
    public void startFrame() {

    }

    @Override
    public void endFrame() {
        executedInFrame = false;
    }

    @Override
    public void dispose() {

    }

    private class WorkerFieldOutput<T> implements FieldOutput<T>, OutputValue<T> {
        private String fieldType;
        private T value;

        public WorkerFieldOutput(String fieldType) {
            this.fieldType = fieldType;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public String getPropertyType() {
            return fieldType;
        }

        @Override
        public T getValue(PipelineRenderingContext context, PipelineRequirements pipelineRequirements) {
            if (!executedInFrame) {
                executeJob(context, workerFieldOutputs);
            }
            return value;
        }
    }

    protected interface OutputValue<T> {
        void setValue(T value);
    }
}
