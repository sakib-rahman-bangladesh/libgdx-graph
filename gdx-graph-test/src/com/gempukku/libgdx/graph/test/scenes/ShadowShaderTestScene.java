package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
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
import com.gempukku.libgdx.graph.plugin.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPublicData;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.adapter.CommonPropertiesModelInstanceRenderableModelAdapter;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginPublicData;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.time.TimeKeeper;

import java.io.IOException;
import java.io.InputStream;

public class ShadowShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;
    private Array<Disposable> disposables = new Array<>();
    private float cameraPositionAngle = MathUtils.PI;
    private Stage stage;

    @Override
    public void initializeScene() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100;
        updateCamera();

        pipelineRenderer = loadPipelineRenderer();

        float halfSize = 5;

        ModelBuilder modelBuilder = new ModelBuilder();
        Model wall = modelBuilder.createBox(halfSize * 2, halfSize * 2, 0.1f, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Model sphere = modelBuilder.createSphere(2, 2, 2, 50, 50, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        disposables.add(wall);
        disposables.add(sphere);

        ModelInstance wallInstance = new ModelInstance(wall);
        wallInstance.transform.trn(0, 0, -3);
        ModelInstance sphereInstance = new ModelInstance(sphere);

        GraphModels graphModels = pipelineRenderer.getPluginData(GraphModels.class);

        CommonPropertiesModelInstanceRenderableModelAdapter wallAdapter = new CommonPropertiesModelInstanceRenderableModelAdapter(wallInstance, graphModels);
        wallAdapter.getPropertyContainer().setValue("Color", Color.LIGHT_GRAY);
        CommonPropertiesModelInstanceRenderableModelAdapter sphereAdapter = new CommonPropertiesModelInstanceRenderableModelAdapter(sphereInstance, graphModels);
        sphereAdapter.getPropertyContainer().setValue("Color", Color.RED);

        wallAdapter.register("Color");
        sphereAdapter.register("Color");

        Lighting3DPublicData lighting = pipelineRenderer.getPluginData(Lighting3DPublicData.class);
        Lighting3DEnvironment environment = new Lighting3DEnvironment();
        environment.setAmbientColor(new Color(0.2f, 0.2f, 0.2f, 1f));
        Directional3DLight directionalLight = new Directional3DLight();
        directionalLight.setColor(Color.WHITE);
        directionalLight.setIntensity(0.3f);
        directionalLight.setDirection(0, 0, -1);
        environment.addDirectionalLight(directionalLight);
        lighting.setEnvironment("Scene", environment);

        stage = createStage();

        Gdx.input.setInputProcessor(stage);

        UIPluginPublicData ui = pipelineRenderer.getPluginData(UIPluginPublicData.class);
        ui.setStage("Stage", stage);
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        final Slider positionAngle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        positionAngle.setValue(MathUtils.PI);
        positionAngle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraPositionAngle = positionAngle.getValue();
                        updateCamera();
                    }
                });
        tbl.add(positionAngle).growX().row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        float cameraDistance = 10f;

        camera.position.set(-cameraDistance * MathUtils.sin(cameraPositionAngle), 0, -cameraDistance * MathUtils.cos(cameraPositionAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0f, 0f, 0f);
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
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("test/shadow-shader-test.json").read();
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
    }
}