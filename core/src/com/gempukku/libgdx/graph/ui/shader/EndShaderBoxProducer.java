package com.gempukku.libgdx.graph.ui.shader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.EndShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.ui.ShaderPreviewWidget;

public class EndShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    private NodeConfiguration<ShaderFieldType> configuration = new EndShaderNodeConfiguration();

    public EndShaderBoxProducer() {
        super(new EndShaderNodeConfiguration());
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

    private static class ShaderPreviewBoxPart extends Table implements GraphBoxPart<ShaderFieldType> {
        private final ShaderPreviewWidget shaderPreviewWidget;

        public ShaderPreviewBoxPart(Skin skin) {
            super(skin);
            shaderPreviewWidget = new ShaderPreviewWidget(200, 200);
            final SelectBox<ShaderPreviewWidget.ShaderPreviewModel> selectBox = new SelectBox<ShaderPreviewWidget.ShaderPreviewModel>(skin);
            selectBox.setItems(ShaderPreviewWidget.ShaderPreviewModel.values());
            add("Preview model: ");
            add(selectBox).growX().row();
            add(shaderPreviewWidget).colspan(2).grow().row();

            selectBox.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            shaderPreviewWidget.setModel(selectBox.getSelected());
                        }
                    });
        }

        public void initialize(JsonValue data) {
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public GraphBoxOutputConnector<ShaderFieldType> getOutputConnector() {
            return null;
        }

        @Override
        public GraphBoxInputConnector<ShaderFieldType> getInputConnector() {
            return null;
        }

        @Override
        public void serializePart(JsonValue object) {
        }

        public void graphChanged(boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
            shaderPreviewWidget.graphChanged(hasErrors, graph);
        }

        @Override
        public void dispose() {
            shaderPreviewWidget.dispose();
        }
    }
}
