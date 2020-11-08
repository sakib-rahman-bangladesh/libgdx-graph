package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

import java.io.IOException;
import java.io.InputStream;

public class LibgdxGraphTestApplication extends ApplicationAdapter {
    private long lastProcessedInput;

    private Array<Disposable> disposables = new Array<>();
    private PipelineRenderer pipelineRenderer;

    private Camera camera;
    private Stage stage;
    private GraphShaderEnvironment lights;
    private Model starfield;
    private Model blackHole;

    private float cameraPositionAngle;
    private float cameraAngle;
    private String blackHoleInstance;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

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

        starfield = modelBuilder.createSphere(100, 100, 100, 50, 50,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(starfield);

        float blackHoleSize = 10f;
        blackHole = modelBuilder.createSphere(blackHoleSize, blackHoleSize, blackHoleSize, 50, 50,
                new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(blackHole);

        registerModels(models);
    }

    private void registerModels(GraphShaderModels models) {
        String starfieldId = models.registerModel(starfield);
        models.addModelDefaultTag(starfieldId, "starfield");
        models.createModelInstance(starfieldId);

        String blackHoleId = models.registerModel(blackHole);
        models.addModelDefaultTag(blackHoleId, "black-hole");
        blackHoleInstance = models.createModelInstance(blackHoleId);
    }

    private Stage createStage() {
        Skin skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        disposables.add(skin);

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

//        final Slider positionAngle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
//        positionAngle.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent event, Actor actor) {
//                        cameraPositionAngle = positionAngle.getValue();
//                        updateCamera();
//                    }
//                });
//
        final Slider angle = new Slider(0, MathUtils.PI2, 0.001f, false, skin);
        angle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        cameraAngle = angle.getValue();
                        updateCamera();
                    }
                });

//        tbl.add("Camera orbit");
//        tbl.add(positionAngle).width(500);
//        tbl.row();
//
        tbl.add("Camera rotation");
        tbl.add(angle).width(500);
        tbl.row();

        stage.addActor(tbl);
        return stage;
    }

    private void updateCamera() {
        float cameraDistance = 30f;

        camera.position.set(cameraDistance * MathUtils.cos(cameraPositionAngle), 0, cameraDistance * MathUtils.sin(cameraPositionAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0f, 0f, 0f);
        camera.rotate(MathUtils.radDeg * cameraAngle, 0, 1, 0);
        //camera.direction.set(MathUtils.cos(cameraAngle), 0, MathUtils.sin(cameraAngle));
        camera.update();

        GraphShaderModels models = pipelineRenderer.getGraphShaderModels();
        Vector3 blackHoleScreenPosition = camera.project(new Vector3(0, 0, 0));
        Vector2 screenPos = new Vector2(blackHoleScreenPosition.x / camera.viewportWidth, blackHoleScreenPosition.y / camera.viewportHeight);
        models.setProperty(blackHoleInstance, "Center Screen Position", screenPos);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        cameraPositionAngle += delta * 0.02f;
        updateCamera();

        reloadRendererIfNeeded();
        stage.act(delta);

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    private void reloadRendererIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (lastProcessedInput + 200 < currentTime) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                lastProcessedInput = currentTime;
                pipelineRenderer.dispose();
                pipelineRenderer = loadPipelineRenderer();
                registerModels(pipelineRenderer.getGraphShaderModels());
            }
        }
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        pipelineRenderer.dispose();
        WhitePixel.dispose();

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
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