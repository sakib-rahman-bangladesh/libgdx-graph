package com.gempukku.libgdx.graph.pipeline.producer.math.geometric;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.CrossProductPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class CrossProductPipelineNodeProducer extends PipelineNodeProducerImpl {
    public CrossProductPipelineNodeProducer() {
        super(new CrossProductPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(final JsonValue data, final ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("a");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("b");
        final Vector3 tmpVector = new Vector3();

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                Vector3 a = (Vector3) aFunction.getValue(pipelineRenderingContext, null);
                Vector3 b = (Vector3) bFunction.getValue(pipelineRenderingContext, null);

                Vector3 result = tmpVector.set(a).crs(b);

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

}
