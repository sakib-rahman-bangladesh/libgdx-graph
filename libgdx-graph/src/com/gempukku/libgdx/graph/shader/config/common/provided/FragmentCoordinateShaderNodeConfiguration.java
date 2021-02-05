package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FragmentCoordinateShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public FragmentCoordinateShaderNodeConfiguration() {
        super("FragmentCoordinate", "Fragment coordinate", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Vector4));
    }
}
