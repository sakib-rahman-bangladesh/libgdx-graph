package com.gempukku.libgdx.graph.shader.config.common.provided;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class SceneDepthShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SceneDepthShaderNodeConfiguration() {
        super("SceneDepth", "Scene depth", "Provided");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("screenPosition", "Screen position", ShaderFieldType.Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("depth", "Depth", ShaderFieldType.Float));
    }
}
