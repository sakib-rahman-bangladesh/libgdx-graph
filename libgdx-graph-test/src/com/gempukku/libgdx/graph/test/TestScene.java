package com.gempukku.libgdx.graph.test;

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
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import com.gempukku.libgdx.graph.shader.models.Transforms;

import java.io.IOException;
import java.io.InputStream;

public class TestScene implements LibgdxGraphTestScene {
    private Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private GraphShaderEnvironment lights;
    private Model sphere;

    private float cameraPosition;
    private String sphereId;

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

        updateCamera();
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

        return camera;
    }

    private void createModels(GraphShaderModels models) {
        ModelBuilder modelBuilder = new ModelBuilder();

        sphere = modelBuilder.createSphere(1, 1, 1, 50, 50,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(sphere);

        registerModels(models);
    }

    private void registerModels(GraphShaderModels models) {
        String sphereId = models.registerModel(sphere);
        models.addModelDefaultTag(sphereId, "default");
        this.sphereId = models.createModelInstance(sphereId);
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        final Slider cameraPositionSlider = new Slider(-3f, 3f, 0.001f, false, skin);
        cameraPositionSlider.setValue(0f);
        cameraPositionSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraPosition = cameraPositionSlider.getValue();
                        updateCamera();
                    }
                });

        final Slider modelPositionSlider = new Slider(-3f, 3f, 0.001f, false, skin);
        modelPositionSlider.setValue(0f);
        modelPositionSlider.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        pipelineRenderer.getGraphShaderModels().updateTransform(
                                sphereId, Transforms.create().idt().translate(modelPositionSlider.getValue(), 0, 0));
                    }
                });

        tbl.add("Camera position");
        tbl.add(cameraPositionSlider).width(500);
        tbl.row();

        tbl.add("Model position");
        tbl.add(modelPositionSlider).width(500);
        tbl.row();

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        float cameraDistance = 30f;

        camera.position.set(-cameraPosition, 0, 2);
        camera.up.set(0f, 1f, 0f);
        camera.direction.set(0, 0, -1);
        //camera.direction.set(MathUtils.cos(cameraAngle), 0, MathUtils.sin(cameraAngle));
        camera.update();
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
//        cameraPositionAngle += delta * 0.1f;
//        updateCamera();

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
            InputStream stream = Gdx.files.local("test.json").read();
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
}