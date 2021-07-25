package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ApplyNormalMapShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ApplyNormalMapShaderNodeConfiguration() {
        super("ApplyNormalMap", "Apply normal map", "Lighting");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("tangent", "Tangent", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normalMap", "Normal from map", true, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("strength", "Strength", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Normal", ShaderFieldType.Vector3));
    }
}
