package com.gempukku.libgdx.graph.plugin.models.config.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class ModelFragmentCoordinateShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ModelFragmentCoordinateShaderNodeConfiguration() {
        super("ModelFragmentCoordinate", "Model fragment coordinate", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Vector2));
    }
}
