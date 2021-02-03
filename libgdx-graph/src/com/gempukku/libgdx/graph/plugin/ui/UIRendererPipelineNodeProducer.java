package com.gempukku.libgdx.graph.plugin.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class UIRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public UIRendererPipelineNodeProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(final JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);
                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);
                Stage stage = pipelineRenderingContext.getPrivatePluginData(UIPluginPrivateData.class).getStage(data.getString("id", ""));
                if (enabled && stage != null) {
                    // Sadly need to switch off (and then on) the RenderContext
                    pipelineRenderingContext.getRenderContext().end();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();
                    int width = currentBuffer.getWidth();
                    int height = currentBuffer.getHeight();
                    int screenWidth = stage.getViewport().getScreenWidth();
                    int screenHeight = stage.getViewport().getScreenHeight();
                    if (screenWidth != width || screenHeight != height)
                        stage.getViewport().update(width, height, true);

                    currentBuffer.beginColor();
                    stage.draw();
                    currentBuffer.endColor();

                    pipelineRenderingContext.getRenderContext().begin();
                }
                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }
        };
    }
}
