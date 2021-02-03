package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;

public class GraphShaderTemplateRegistry {
    public static Array<GraphShaderTemplate> modelShaderTemplateList = new Array<>();
    public static Array<GraphShaderTemplate> screenShaderTemplateList = new Array<>();

    static {
        modelShaderTemplateList.add(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/model/empty-model-shader.json")));
        modelShaderTemplateList.add(null);
        modelShaderTemplateList.add(
                new LoadFileGraphShaderTemplate("From file..."));
        modelShaderTemplateList.add(
                new PasteGraphShaderTemplate(GraphDesignTab.Type.Model_Shader));

        screenShaderTemplateList.add(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/screen/empty-screen-shader.json")));
        screenShaderTemplateList.add(null);
        screenShaderTemplateList.add(
                new LoadFileGraphShaderTemplate("From file..."));
        screenShaderTemplateList.add(
                new PasteGraphShaderTemplate(GraphDesignTab.Type.Screen_Shader));
    }

    private GraphShaderTemplateRegistry() {

    }
}
