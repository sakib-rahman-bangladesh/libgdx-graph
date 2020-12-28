package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class UVFlipbookShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public UVFlipbookShaderNodeConfiguration() {
        super("UVFlipbook", "UV Flipbook", "Texture");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("tileCount", "Tile count", true, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("index", "Index", true, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("looping", "Looping", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "UV", ShaderFieldType.Vector2));
    }
}
