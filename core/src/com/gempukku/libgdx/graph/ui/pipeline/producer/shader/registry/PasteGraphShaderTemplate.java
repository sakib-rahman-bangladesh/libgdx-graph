package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.ui.LibgdxGraphScreen;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;

public class PasteGraphShaderTemplate implements GraphShaderTemplate {
    private GraphDesignTab.Type graphType;

    public PasteGraphShaderTemplate(GraphDesignTab.Type graphType) {
        this.graphType = graphType;
    }

    @Override
    public String getTitle() {
        return "Paste";
    }

    @Override
    public void invokeTemplate(Stage stage, Callback callback) {
        if (graphType == LibgdxGraphScreen.graphInClipboard.graphType) {
            callback.addShader("", LibgdxGraphScreen.graphInClipboard.graph);
        }
    }
}
