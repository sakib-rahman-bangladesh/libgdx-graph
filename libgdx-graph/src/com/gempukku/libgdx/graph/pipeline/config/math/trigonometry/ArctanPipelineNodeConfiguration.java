package com.gempukku.libgdx.graph.pipeline.config.math.trigonometry;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class ArctanPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ArctanPipelineNodeConfiguration() {
        super("Arctan", "Arctangent", "Math/Trigonometry");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        new SameTypeOutputTypeFunction<PipelineFieldType>("input"),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3));
    }
}
