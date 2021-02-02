package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
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
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.shader.TransformUpdate;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
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

public class Episode9Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Model shipModel;
    private Model robotModel;

    private Camera camera;
    private Stage stage;
    private Skin skin;
    private GraphShaderEnvironment lights;
    private float cameraAngle = 0f;
    private float cameraDistance = 1.6f;
    private float robotScale = 0.0008f;
    private float robotDistance = 0.9f;
    private float robotAngle = 0f;
    private float robotSpeed = -0.4f;
    private AnimationController robotAnimation;
    private GraphModelInstance robotInstance;
    private AnimationController robot2Animation;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        lights = createLights();
        stage = createStage();

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

        GraphModel shipModelId = models.registerModel(this.shipModel);
        final float shipScale = 0.0008f;
        GraphModelInstance shipModelInstance = models.createModelInstance(shipModelId);
        models.updateTransform(shipModelInstance,
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.scale(shipScale, shipScale, shipScale).rotate(-1, 0, 0f, 90);
                    }
                });
        models.addTag(shipModelInstance, "Environment", TagOptimizationHint.Always);

        GraphModel robotModelId = models.registerModel(this.robotModel);

        robotInstance = models.createModelInstance(robotModelId);
        models.addTag(robotInstance, "Seen-through", TagOptimizationHint.Always);
        robotAnimation = models.createAnimationController(robotInstance);
        robotAnimation.animate("Root|jog", -1, null, 0f);

        GraphModelInstance robot2 = models.createModelInstance(robotModelId);
        models.updateTransform(robot2,
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.translate(0.25f, 0, 0.9f).scale(robotScale, robotScale, robotScale);
                    }
                });
        models.addTag(robot2, "Seen-through", TagOptimizationHint.Always);
        robot2Animation = models.createAnimationController(robot2);
        robot2Animation.animate("Root|idle", -1, null, 0f);
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
        robotAnimation.update(delta);
        robot2Animation.update(delta);

        robotAngle += delta * robotSpeed;
        pipelineRenderer.getGraphShaderModels().updateTransform(robotInstance,
                new TransformUpdate() {
                    @Override
                    public void updateTransform(Matrix4 transform) {
                        transform.idt()
                                .translate(0.9f * robotDistance * MathUtils.sin(robotAngle), 0, 0.2f + robotDistance * MathUtils.cos(robotAngle))
                                .rotate(0, 1f, 0, MathUtils.radiansToDegrees * robotAngle - 90)
                                .scale(robotScale, robotScale, robotScale);
                    }
                });

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
        pipelineRenderer.setPipelineProperty("Lights", lights);
        pipelineRenderer.getPluginData(UIPluginPublicData.class).setStage("", stage);
    }
}