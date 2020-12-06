package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class GraphShaderTemplateRegistry {
    public static Array<GraphShaderTemplate> graphShaderTemplateList = new Array<>();
    public static Array<GraphShaderTemplate> particlesShaderTemplateList = new Array<>();

    static {
        graphShaderTemplateList.add(
                new FileGraphShaderTemplate("Empty", Gdx.files.classpath("template/empty-shader.json")));
        graphShaderTemplateList.add(
                new LoadFileGraphShaderTemplate("From file..."));

        particlesShaderTemplateList.add(
                new FileGraphShaderTemplate("Empty billboard", Gdx.files.classpath("template/empty-billboard-particles.json")));
    }

    private GraphShaderTemplateRegistry() {

    }
}
