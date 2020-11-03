package com.gempukku.libgdx.graph.shader.config.shape;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class EllipseShapeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public EllipseShapeShaderNodeConfiguration() {
        super("EllipseShape", "Ellipse Shape", "Shape");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("size", "Size", false, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("width", "Width", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Float));
    }
}
