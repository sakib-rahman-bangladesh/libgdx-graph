package com.gempukku.libgdx.graph.ui.shader.model.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.EndModelShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.ShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class EndModelShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    private NodeConfiguration<ShaderFieldType> configuration = new EndModelShaderNodeConfiguration();

    public EndModelShaderBoxProducer() {
        super(new EndModelShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ShaderPreviewBoxPart previewBoxPart = new ShaderPreviewBoxPart(skin);
        previewBoxPart.initialize(data);

        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, configuration, skin) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        addConfigurationInputsAndOutputs(skin, result);
        SelectBoxPart<ShaderFieldType> cullingBox = new SelectBoxPart<>(skin, "Culling", "culling", BasicShader.Culling.values());
        cullingBox.initialize(data);
        result.addGraphBoxPart(cullingBox);
        SelectBoxPart<ShaderFieldType> transparencyBox = new SelectBoxPart<>(skin, "Transparency", "transparency", BasicShader.Transparency.values());
        transparencyBox.initialize(data);
        result.addGraphBoxPart(transparencyBox);
        SelectBoxPart<ShaderFieldType> blendingBox = new SelectBoxPart<>(skin, "Blending", "blending", BasicShader.Blending.values());
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);
        SelectBoxPart<ShaderFieldType> depthTestBox = new SelectBoxPart<>(skin, "DepthTest", "depthTest", BasicShader.DepthTesting.values());
        depthTestBox.initialize(data);
        result.addGraphBoxPart(depthTestBox);

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }

    @Override
    public GraphBox<ShaderFieldType> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }
}
