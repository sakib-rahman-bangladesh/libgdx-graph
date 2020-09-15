package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ApplyBumpMapShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ApplyBumpMapShaderNodeConfiguration() {
        super("ApplyBumpMap", "Apply bump map", "Lighting");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("tangent", "Tangent", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("bump", "Bump normal", true, ShaderFieldType.Vector3, ShaderFieldType.Color));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Normal", ShaderFieldType.Vector3));
    }
}
