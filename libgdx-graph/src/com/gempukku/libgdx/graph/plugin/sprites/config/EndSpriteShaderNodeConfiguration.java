package com.gempukku.libgdx.graph.plugin.sprites.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class EndSpriteShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public EndSpriteShaderNodeConfiguration() {
        super("SpriteShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("position", "Position", false, false, ShaderFieldType.Vector2));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("layer", "Layer", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("anchor", "Anchor", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("size", "Size", false, false, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("rotation", "Rotation", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("color", "Color", false, false,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
