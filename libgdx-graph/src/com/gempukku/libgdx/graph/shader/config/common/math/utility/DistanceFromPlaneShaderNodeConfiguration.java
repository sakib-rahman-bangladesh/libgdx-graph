package com.gempukku.libgdx.graph.shader.config.common.math.utility;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DistanceFromPlaneShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public DistanceFromPlaneShaderNodeConfiguration() {
        super("DistancePlane", "Distance from plane", "Math/Utility");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("planePoint", "Point on plane", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("planeNormal", "Normal to plane", true, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Distance", ShaderFieldType.Float));
    }
}
