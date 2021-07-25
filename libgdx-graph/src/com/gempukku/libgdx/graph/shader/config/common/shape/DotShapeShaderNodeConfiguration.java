package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DotShapeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public DotShapeShaderNodeConfiguration() {
        super("DotShape", "Dot Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Float));
    }
}
