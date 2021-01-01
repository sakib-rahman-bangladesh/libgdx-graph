package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class BorderDetectionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public BorderDetectionShaderNodeConfiguration() {
        super("BorderDetection", "Border detection", "Texture");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("pixelSize", "Pixel size", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("outlineWidth", "Outline width", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("alphaEdge", "Alpha edge", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("left", "Left", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("right", "Right", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("up", "Up", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("down", "Down", ShaderFieldType.Float));
    }
}
