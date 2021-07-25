package com.gempukku.libgdx.graph.shader.config.common.effect;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FresnelEffectShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public FresnelEffectShaderNodeConfiguration() {
        super("FresnelEffect", "Fresnel effect", "Effect");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("viewDir", "View direction", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("power", "Power", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Float));
    }
}
