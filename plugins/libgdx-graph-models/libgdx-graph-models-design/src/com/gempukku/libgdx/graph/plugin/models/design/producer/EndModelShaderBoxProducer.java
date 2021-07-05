package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.plugin.models.config.EndModelShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class EndModelShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public EndModelShaderBoxProducer() {
        super(new EndModelShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart();
        previewBoxPart.initialize(data);

        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, getConfiguration()) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        addConfigurationInputsAndOutputs(result);
        SelectBoxPart<ShaderFieldType> cullingBox = new SelectBoxPart<>("Culling", "culling", BasicShader.Culling.values());
        cullingBox.initialize(data);
        result.addGraphBoxPart(cullingBox);
        SelectBoxPart<ShaderFieldType> blendingBox = new SelectBoxPart<>("Blending", "blending", BasicShader.Blending.values());
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);
        SelectBoxPart<ShaderFieldType> depthTestBox = new SelectBoxPart<>("DepthTest", "depthTest", BasicShader.DepthTesting.values());
        depthTestBox.initialize(data);
        result.addGraphBoxPart(depthTestBox);

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }
}
