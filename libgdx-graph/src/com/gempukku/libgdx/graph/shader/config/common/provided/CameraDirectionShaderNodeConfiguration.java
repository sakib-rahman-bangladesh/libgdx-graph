package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class CameraDirectionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public CameraDirectionShaderNodeConfiguration() {
        super("CameraDirection", "Camera direction", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("direction", "Direction", ShaderFieldType.Vector3));
    }
}
