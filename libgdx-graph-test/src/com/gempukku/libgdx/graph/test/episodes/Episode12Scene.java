package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.model.GraphModels;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;

import java.io.IOException;
import java.io.InputStream;

public class Episode12Scene implements LibgdxGraphTestScene {
    private Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private Skin skin;
    private GraphShaderEnvironment lights;
    private String sphereInstanceId;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();
        disposables.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getGraphShaderModels());

        Gdx.input.setInputProcessor(stage);
    }

    private GraphShaderEnvironment createLights() {
        float ambientBrightness = 0.3f;
        float directionalBrightness = 0.8f;
        GraphShaderEnvironment lights = new GraphShaderEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-0.3f, -0.4f, -1);
        lights.addDirectionalLight(directionalLight);
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(5, 2, 5);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0, 0f);
        camera.update();

        return camera;
    }

    private void createModels(GraphModels models) {
        float radius = 3.5f;
        ModelBuilder modelBuilder = new ModelBuilder();
        Model sphere = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        disposables.add(sphere);

        String sphereId = models.registerModel(sphere);

        models.addModelDefaultTag(sphereId, "dissolve");

        sphereInstanceId = models.createModelInstance(sphereId);
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        final Slider dissolve = new Slider(-5, 5, 0.01f, false, skin);
        dissolve.setValue(0f);
        addListener(dissolve, "Dissolve");
        final Slider noiseScale = new Slider(0, 10, 0.01f, false, skin);
        noiseScale.setValue(3f);
        addListener(noiseScale, "Noise Scale");
        final Slider noiseValue = new Slider(0, 10, 0.01f, false, skin);
        noiseValue.setValue(1.5f);
        addListener(noiseValue, "Noise Value");

        tbl.add("Dissolve").padRight(5f);
        tbl.add(dissolve).width(300f);
        tbl.row();

        tbl.add("Noise Scale").padRight(5f);
        tbl.add(noiseScale).width(300f);
        tbl.row();

        tbl.add("Noise Value").padRight(5f);
        tbl.add(noiseValue).width(300f);
        tbl.row();

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();

        stage.act(delta);

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode12.json").read();
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
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.setPipelineProperty("Lights", lights);
        pipelineRenderer.setPipelineProperty("Stage", stage);
    }

    private void addListener(final Slider slider, final String propertyName) {
        slider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GraphModels models = pipelineRenderer.getGraphShaderModels();
                        models.setProperty(sphereInstanceId, propertyName, slider.getValue());
                    }
                });
    }
}