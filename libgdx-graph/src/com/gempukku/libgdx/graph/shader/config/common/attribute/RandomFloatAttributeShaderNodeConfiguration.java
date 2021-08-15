package com.gempukku.libgdx.graph.shader.config.common.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class RandomFloatAttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public RandomFloatAttributeShaderNodeConfiguration() {
        super("RandomFloatAttribute", "Random float attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Float));
    }
}
