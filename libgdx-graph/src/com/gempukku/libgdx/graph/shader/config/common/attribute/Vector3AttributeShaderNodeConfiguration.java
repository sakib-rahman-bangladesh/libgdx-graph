package com.gempukku.libgdx.graph.shader.config.common.attribute;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class Vector3AttributeShaderNodeConfiguration extends NodeConfigurationImpl {
    public Vector3AttributeShaderNodeConfiguration() {
        super("Vector3Attribute", "Vector3 attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", ShaderFieldType.Vector3));
    }
}
