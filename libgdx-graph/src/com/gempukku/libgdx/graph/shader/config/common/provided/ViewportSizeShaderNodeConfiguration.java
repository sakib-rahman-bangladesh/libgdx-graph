package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ViewportSizeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ViewportSizeShaderNodeConfiguration() {
        super("ViewportSize", "Viewport size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("size", "Size", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("x", "X", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("y", "Y", ShaderFieldType.Float));
    }
}
