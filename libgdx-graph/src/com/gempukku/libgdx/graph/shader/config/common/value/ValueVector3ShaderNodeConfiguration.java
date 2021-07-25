package com.gempukku.libgdx.graph.shader.config.common.value;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

import static com.gempukku.libgdx.graph.shader.field.ShaderFieldType.Vector3;

public class ValueVector3ShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueVector3ShaderNodeConfiguration() {
        super("ValueVector3", "Vector3", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", Vector3));
    }
}
