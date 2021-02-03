package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class ModelShaderGraphType extends GraphType {
    public static ModelShaderGraphType instance = new ModelShaderGraphType();

    public ModelShaderGraphType() {
        super("Model_Shader", true);
    }
}
