package com.gempukku.libgdx.graph.shader.config.common.shape;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class RectangleShapeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public RectangleShapeShaderNodeConfiguration() {
        super("RectangleShape", "Rectangle Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("size", "Size", false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("border", "Border", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Float));
    }
}
