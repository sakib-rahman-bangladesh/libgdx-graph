package com.gempukku.libgdx.graph.pipeline.producer.rendering.node;

import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineInitializationFeedback;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

public class EndPipelineNode implements PipelineNode {
    private PipelineNode.FieldOutput<RenderPipeline> renderPipeline;
    private PipelineRequirements pipelineRequirements = new PipelineRequirements();

    public EndPipelineNode(PipelineNode.FieldOutput<RenderPipeline> renderPipeline) {
        this.renderPipeline = renderPipeline;
    }

    @Override
    public void initializePipeline(PipelineInitializationFeedback pipelineInitializationFeedback) {

    }

    @Override
    public FieldOutput<?> getFieldOutput(String name) {
        return null;
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

    public RenderPipeline executePipeline(PipelineRenderingContext pipelineRenderingContext) {
        pipelineRequirements.reset();
        return renderPipeline.getValue(pipelineRenderingContext, pipelineRequirements);
    }
}
