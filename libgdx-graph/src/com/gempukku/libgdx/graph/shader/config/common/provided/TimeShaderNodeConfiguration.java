package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class TimeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public TimeShaderNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("time", "Time", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("sinTime", "sin(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("cosTime", "cos(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("deltaTime", "deltaTime", ShaderFieldType.Float));
    }
}
