package com.gempukku.libgdx.graph.shader.config.common.texture;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Sampler2DShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public Sampler2DShaderNodeConfiguration() {
        super("Sampler2D", "Sampler 2D", "Texture");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("texture", "Texture", true, ShaderFieldType.TextureRegion));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Vector4));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("r", "R", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("g", "G", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("b", "B", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("a", "A", ShaderFieldType.Float));
    }
}
