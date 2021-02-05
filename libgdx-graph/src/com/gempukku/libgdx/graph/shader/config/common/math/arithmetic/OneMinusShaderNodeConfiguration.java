package com.gempukku.libgdx.graph.shader.config.common.math.arithmetic;

import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class OneMinusShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public OneMinusShaderNodeConfiguration() {
        super("OneMinus", "One minus", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("input", "Input", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new SameTypeOutputTypeFunction<ShaderFieldType>("input"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
