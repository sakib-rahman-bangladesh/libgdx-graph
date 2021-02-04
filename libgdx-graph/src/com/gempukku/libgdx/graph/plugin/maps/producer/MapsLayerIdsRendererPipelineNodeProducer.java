package com.gempukku.libgdx.graph.plugin.maps.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.plugin.maps.MapsPluginPrivateData;

public class MapsLayerIdsRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public MapsLayerIdsRendererPipelineNodeProducer() {
        super(new MapsLayerIdsRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(final JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);
                Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                boolean enabled = processorEnabled == null || processorEnabled.getValue(pipelineRenderingContext, null);
                Map map = pipelineRenderingContext.getPrivatePluginData(MapsPluginPrivateData.class).getMap(data.getString("id"));
                MapRenderer mapRenderer = pipelineRenderingContext.getPrivatePluginData(MapsPluginPrivateData.class).getMapRenderer(data.getString("id"));
                if (enabled && map != null) {
                    // Sadly need to switch off (and then on) the RenderContext
                    pipelineRenderingContext.getRenderContext().end();

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    String[] layerIds = data.getString("layers").split(",");
                    int[] ids = new int[layerIds.length];
                    for (int i = 0; i < layerIds.length; i++) {
                        ids[i] = Integer.parseInt(layerIds[i]);
                    }

                    currentBuffer.beginColor();
                    mapRenderer.setView((OrthographicCamera) camera);
                    mapRenderer.render(ids);
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
