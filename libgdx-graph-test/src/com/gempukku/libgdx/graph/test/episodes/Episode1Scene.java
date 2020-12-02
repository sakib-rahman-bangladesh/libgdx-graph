package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;

import java.io.IOException;
import java.io.InputStream;

public class Episode1Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;

    @Override
    public void initializeScene() {
        pipelineRenderer = loadPipelineRenderer();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {

    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode1.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback());
                setupPipeline(pipelineRenderer);
                return pipelineRenderer;
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }

    private void setupPipeline(PipelineRenderer pipelineRenderer) {
        pipelineRenderer.setPipelineProperty("Background Color", Color.ORANGE);
    }
}