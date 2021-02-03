package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class RenderPipelineGraphType extends GraphType {
    public static RenderPipelineGraphType instance = new RenderPipelineGraphType();

    public RenderPipelineGraphType() {
        super("Render_Pipeline", false);
    }
}
