package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.CustomRenderCallback;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.CustomRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class CustomRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public CustomRendererPipelineNodeProducer() {
        super(new CustomRendererPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<CustomRenderCallback> callbackInput = (PipelineNode.FieldOutput<CustomRenderCallback>) inputFields.get("callback");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderContext renderContext = pipelineRenderingContext.getRenderContext();

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);
                CustomRenderCallback callback = callbackInput.getValue(pipelineRenderingContext, pipelineRequirements);

                renderContext.end();

                callback.renderCallback(renderPipeline, pipelineRenderingContext, pipelineRequirements);

                renderContext.begin();

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }
        };
    }
}
