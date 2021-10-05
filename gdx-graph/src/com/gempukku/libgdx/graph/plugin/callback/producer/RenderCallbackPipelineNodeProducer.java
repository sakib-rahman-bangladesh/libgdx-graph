package com.gempukku.libgdx.graph.plugin.callback.producer;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallback;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPrivateData;

public class RenderCallbackPipelineNodeProducer extends PipelineNodeProducerImpl {
    public RenderCallbackPipelineNodeProducer() {
        super(new RenderCallbackPipelineNodeConfiguration());
    }

    @Override
    protected PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final String callbackId = data.getString("callbackId", null);
        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);
                if (enabled) {
                    RenderCallbackPrivateData renderCallbackData = pipelineRenderingContext.getPrivatePluginData(RenderCallbackPrivateData.class);

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    RenderCallback callback = renderCallbackData.getRenderCallback(callbackId);

                    renderContext.end();

                    callback.renderCallback(renderPipeline, pipelineRenderingContext, pipelineRequirements);

                    renderContext.begin();
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }
        };
    }
}
