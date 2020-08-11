package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;

public interface GraphBoxOutputConnector extends PipelineNodeOutput {
    enum Side {
        Right, Bottom;
    }

    Side getSide();

    float getOffset();
}
