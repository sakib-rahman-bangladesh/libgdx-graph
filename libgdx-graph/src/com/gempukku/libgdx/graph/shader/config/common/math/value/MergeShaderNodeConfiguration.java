package com.gempukku.libgdx.graph.shader.config.common.math.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class MergeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public MergeShaderNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("x", "X", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("y", "Y", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("z", "Z", ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("w", "W", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("v2", "Vector2", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("v3", "Vector3", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Vector4));
    }
}
