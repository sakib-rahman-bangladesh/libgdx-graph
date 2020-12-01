package com.gempukku.libgdx.graph.ui.part;

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
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.shader.model.producer.ShaderPreviewWidget;

public class ShaderPreviewBoxPart extends Table implements GraphBoxPart<ShaderFieldType> {
    private final ShaderPreviewWidget shaderPreviewWidget;
    private final SelectBox<ShaderPreviewWidget.ShaderPreviewModel> selectBox;

    public ShaderPreviewBoxPart(Skin skin) {
        super(skin);
        shaderPreviewWidget = new ShaderPreviewWidget(200, 200);
        selectBox = new SelectBox<ShaderPreviewWidget.ShaderPreviewModel>(skin);
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

    public void setPreviewModel(ShaderPreviewWidget.ShaderPreviewModel previewModel) {
        shaderPreviewWidget.setModel(previewModel);
        selectBox.setSelected(previewModel);
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