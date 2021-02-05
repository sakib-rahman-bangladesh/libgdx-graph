package com.gempukku.libgdx.graph.shader.config.common.value;


import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

import static com.gempukku.libgdx.graph.shader.ShaderFieldType.Vector4;

public class ValueColorShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueColorShaderNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", Vector4));
    }
}
