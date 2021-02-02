package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.shader.TransformUpdate;
import com.gempukku.libgdx.graph.shader.model.GraphModel;
import com.gempukku.libgdx.graph.shader.model.GraphModelInstance;
import com.gempukku.libgdx.graph.shader.model.GraphModels;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.time.TimeKeeper;

import java.io.IOException;
import java.io.InputStream;

public class Episode6Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model model;
    private Camera camera;
    private Stage stage;
    private GraphModelInstance shipInstance;
    private Skin skin;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getGraphShaderModels());

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        camera.position.set(0.7f, 0.3f, 1f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
        return camera;
    }

    private void createModels(GraphModels models) {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.classpath("model/fighter/fighter.g3db"));

        GraphModel modelId = models.registerModel(model);
        final float scale = 0.0008f;
        shipInstance = models.createModelInstance(modelId);
        models.updateTransform(shipInstance,
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.scale(scale, scale, scale).rotate(-1, 0, 0f, 90);
                    }
                });
        models.addTag(shipInstance, "Default", TagOptimizationHint.Temporary);
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        final TextButton switchButton = new TextButton("Normal/Hologram", skin, "toggle");
        switchButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        boolean checked = switchButton.isChecked();
                        String removeTag = checked ? "Default" : "Hologram";
                        String tag = checked ? "Hologram" : "Default";
                        pipelineRenderer.getGraphShaderModels().removeTag(shipInstance, removeTag);
                        pipelineRenderer.getGraphShaderModels().addTag(shipInstance, tag, TagOptimizationHint.Temporary);
                    }
                });

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table();
        tbl.add(switchButton).row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        model.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode6.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback(timeKeeper));
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
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
    }
}