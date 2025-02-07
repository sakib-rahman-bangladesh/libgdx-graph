package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

import java.io.IOException;
import java.io.InputStream;

public class Episode9Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model shipModel;
    private Model robotModel;

    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Lighting3DEnvironment lights;
    private float cameraAngle = 0f;
    private float cameraDistance = 1.6f;
    private float robotScale = 0.0008f;
    private float robotDistance = 0.9f;
    private float robotAngle = 0f;
    private float robotSpeed = -0.4f;
    private AnimationController robot1Animation;
    private AnimationController robot2Animation;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelInstance robot1Instance;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getPluginData(GraphModels.class));

        Gdx.input.setInputProcessor(stage);
    }

    private Lighting3DEnvironment createLights() {
        float ambientBrightness = 0.3f;
        float directionalBrightness = 0.8f;
        Lighting3DEnvironment lights = new Lighting3DEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-0.3f, -0.4f, -1);
        lights.addDirectionalLight(new Directional3DLight(directionalLight));
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        float y = 0.4f;
        camera.position.set(cameraDistance * MathUtils.sin(cameraAngle), y, cameraDistance * MathUtils.cos(cameraAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0, 0f);
        camera.update();

        return camera;
    }

    private void createModels(GraphModels models) {
        this.shipModel = loadShipModel();
        this.robotModel = loadRobotModel();

        ModelInstance shipModelInstance = new ModelInstance(shipModel);
        final float shipScale = 0.0008f;
        shipModelInstance.transform.idt().scale(shipScale, shipScale, shipScale).rotate(-1, 0, 0f, 90);

        MaterialModelInstanceModelAdapter shipAdapter = new MaterialModelInstanceModelAdapter(shipModelInstance, models);
        shipAdapter.addTag("Environment");

        robot1Instance = new ModelInstance(robotModel);
        robot1Animation = new AnimationController(robot1Instance);
        robot1Animation.animate("Root|jog", -1, null, 0f);

        MaterialModelInstanceModelAdapter robot1Adapter = new MaterialModelInstanceModelAdapter(robot1Instance, models);
        robot1Adapter.addTag("Seen-through");
        robot1Adapter.addTag("Seen-through-silhouette");

        ModelInstance robot2Instance = new ModelInstance(robotModel);
        robot2Instance.transform.idt().translate(0.25f, 0, 0.9f).scale(robotScale, robotScale, robotScale);
        robot2Animation = new AnimationController(robot2Instance);
        robot2Animation.animate("Root|idle", -1, null, 0f);

        MaterialModelInstanceModelAdapter robot2Adapter = new MaterialModelInstanceModelAdapter(robot2Instance, models);
        robot2Adapter.addTag("Seen-through");
        robot2Adapter.addTag("Seen-through-silhouette");
    }

    private Model loadRobotModel() {
        JsonReader jsonReader = new JsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        return modelLoader.loadModel(Gdx.files.classpath("model/gold-robot/gold-robot.g3dj"));
    }

    private Model loadShipModel() {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        return modelLoader.loadModel(Gdx.files.classpath("model/fighter/fighter.g3db"));
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

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
        robot1Animation.update(delta);
        robot2Animation.update(delta);

        robotAngle += delta * robotSpeed;
        robot1Instance.transform.idt()
                .translate(0.9f * robotDistance * MathUtils.sin(robotAngle), 0, 0.2f + robotDistance * MathUtils.cos(robotAngle))
                .rotate(0, 1f, 0, MathUtils.radiansToDegrees * robotAngle - 90)
                .scale(robotScale, robotScale, robotScale);

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        shipModel.dispose();
        robotModel.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode9.json").read();
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
        pipelineRenderer.getPluginData(Lighting3DPublicData.class).setEnvironment("", lights);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
    }
}