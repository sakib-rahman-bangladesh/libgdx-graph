package com.gempukku.libgdx.graph.shader.config.math.arithmetic;

import com.gempukku.libgdx.graph.MultiParamVectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AddShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AddShaderNodeConfiguration() {
        super("Add", "Add", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("inputs", "Inputs", true, false, true,
                        ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new MultiParamVectorArithmeticOutputTypeFunction<ShaderFieldType>(ShaderFieldType.Float, "inputs"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Color));
    }
}
