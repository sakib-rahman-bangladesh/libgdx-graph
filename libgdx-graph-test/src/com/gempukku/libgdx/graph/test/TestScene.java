package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.camera.CameraController;
import com.gempukku.libgdx.graph.camera.FocusWindowCameraController;
import com.gempukku.libgdx.graph.camera.SpriteFocus;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.graph.sprite.SpriteStateData;
import com.gempukku.libgdx.graph.sprite.StateBasedSprite;

import java.io.IOException;
import java.io.InputStream;

public class TestScene implements LibgdxGraphTestScene {
    private Array<Disposable> resources = new Array<>();
    private PipelineRenderer pipelineRenderer;
    private OrthographicCamera camera;
    private CameraController cameraController;
    private Stage stage;
    private Skin skin;
    private StateBasedSprite doctorSprite;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        stage = createStage();
        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        resources.add(pipelineRenderer);

        createModels(pipelineRenderer.getGraphSprites());

        cameraController = new FocusWindowCameraController(camera, new SpriteFocus(doctorSprite),
                new Rectangle(0.3f, 0.2f, 0.4f, 0.4f),
                new Rectangle(0.4f, 0.4f, 0.2f, 0.2f), new Vector2(0.05f, 0.0f));

        Gdx.input.setInputProcessor(stage);
    }

    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }

    private void createModels(GraphSprites graphSprites) {
        GraphSprite doctor = graphSprites.createSprite(10f, new Vector2(0, -200), new Vector2(450, 450), new Vector2(0.5f, 0.8f), "Animated");

        Texture idleTexture = new Texture(Gdx.files.classpath("image/BlueWizardIdle.png"));
        resources.add(idleTexture);
        Texture walkTexture = new Texture(Gdx.files.classpath("image/BlueWizardWalk.png"));
        resources.add(walkTexture);
        Texture jumpTexture = new Texture(Gdx.files.classpath("image/BlueWizardJump.png"));
        resources.add(jumpTexture);

        ObjectMap<String, SpriteStateData> animationData = new ObjectMap<>();
        animationData.put("Idle", new SpriteStateData(new TextureRegion(idleTexture), 20, 1, 20f, true));
        animationData.put("Walk", new SpriteStateData(new TextureRegion(walkTexture), 5, 4, 20f, true));
        animationData.put("Jump", new SpriteStateData(new TextureRegion(jumpTexture), 8, 1, 20f, false));

        doctorSprite = new StateBasedSprite(doctor, new Vector2(0, -200), new Vector2(450, 450), "Idle", SpriteFaceDirection.Right, animationData);
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        resources.add(skin);

        Stage stage = new Stage(new ScreenViewport());
        resources.add(stage);

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void renderScene() {
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        stage.act(delta);
        float moveSpeed = 200f;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            doctorSprite.moveBy(delta * moveSpeed, 0);
            doctorSprite.setFaceDirection(SpriteFaceDirection.Right);
            doctorSprite.setState("Walk");
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            doctorSprite.moveBy(-delta * moveSpeed, 0);
            doctorSprite.setFaceDirection(SpriteFaceDirection.Left);
            doctorSprite.setState("Walk");
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            doctorSprite.setState("Jump");
        } else {
            doctorSprite.setState("Idle");
        }
        if (doctorSprite.isDirty())
            doctorSprite.updateSprite(pipelineRenderer);

        cameraController.update(delta);

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        for (Disposable resource : resources) {
            resource.dispose();
        }

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
        pipelineRenderer.setPipelineProperty("Stage", stage);
    }


}