package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueVector3PipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueVector3PipelineNodeConfiguration() {
        super("ValueVector3", "Vector3 value");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", "Value", PropertyType.Vector3));
    }
}
