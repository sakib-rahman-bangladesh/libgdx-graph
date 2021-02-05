package com.gempukku.libgdx.graph.plugin.models.config;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class EndModelShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public EndModelShaderNodeConfiguration() {
        super("ShaderEnd", "Shader output", null);
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("position", "World position", false, false, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("color", "Color", false, false, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("alpha", "Alpha", false, false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("alphaClip", "Alpha clip", false, false, ShaderFieldType.Float));
    }
}
