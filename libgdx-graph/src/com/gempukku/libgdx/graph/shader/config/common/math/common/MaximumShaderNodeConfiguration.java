package com.gempukku.libgdx.graph.shader.config.common.math.common;

import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class MaximumShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public MaximumShaderNodeConfiguration() {
        super("Maximum", "Maximum", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new MathCommonOutputTypeFunction<ShaderFieldType>(ShaderFieldType.Float, new String[]{"a"}, new String[]{"b"}),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
