package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class BlinnPhongLightingShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public BlinnPhongLightingShaderNodeConfiguration() {
        super("BlinnPhongLighting", "Blinn-Phong lighting", "Lighting");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("position", "Position", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("albedo", "Albedo", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("emission", "Emission", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("specular", "Specular", false, ShaderFieldType.Vector4, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("ambientOcclusion", "A.Occlusion", false, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("shininess", "Shininess", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Color", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("diffuse", "Diffuse", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("specularOut", "Specular", ShaderFieldType.Vector3));
    }
}
