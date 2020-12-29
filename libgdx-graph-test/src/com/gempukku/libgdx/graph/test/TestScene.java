package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
import com.gempukku.libgdx.graph.entity.GameEntity;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;
import com.gempukku.libgdx.graph.sprite.SpriteFaceDirection;
import com.gempukku.libgdx.graph.sprite.SpriteStateData;
import com.gempukku.libgdx.graph.sprite.StateBasedSprite;
import com.gempukku.libgdx.graph.sprite.TiledSprite;
import com.gempukku.libgdx.graph.system.EntitySystem;
import com.gempukku.libgdx.graph.system.PhysicsSystem;
import com.gempukku.libgdx.graph.system.PlayerControlSystem;

import java.io.IOException;
import java.io.InputStream;

public class TestScene implements LibgdxGraphTestScene {
    private Array<Disposable> resources = new Array<>();
    private PipelineRenderer pipelineRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private CameraController cameraController;
    private PhysicsSystem physicsSystem;
    private EntitySystem entitySystem;
    private PlayerControlSystem playerControlSystem;

    private boolean debugRender = false;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 tmpMatrix;

    @Override
    public void initializeScene() {
        Box2D.init();
        WhitePixel.initialize();

        skin = createSkin();
        resources.add(skin);

        stage = createStage(skin);
        resources.add(stage);

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        resources.add(pipelineRenderer);

        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        physicsSystem = new PhysicsSystem(-30f);
        resources.add(physicsSystem);

        entitySystem = new EntitySystem(pipelineRenderer);
        resources.add(entitySystem);

        playerControlSystem = new PlayerControlSystem();
        resources.add(playerControlSystem);

        GameEntity<StateBasedSprite> playerEntity = entitySystem.createGameEntity(createPlayerSprite());
        playerEntity.createDynamicBody(physicsSystem, new Vector2(0.5f, 0.5f), new Vector2(70f / 256, 150f / 256));

        playerControlSystem.setPlayerEntity(playerEntity);

        GameEntity<TiledSprite> groundEntity = entitySystem.createGameEntity(createGroundSprite());
        groundEntity.createStaticBody(physicsSystem, new Vector2(0.5f, 0.6f), new Vector2(1, 0.8f));

        cameraController = new FocusWindowCameraController(camera, new SpriteFocus(playerEntity.getSprite()),
                new Rectangle(0.1f, 0.1f, 0.4f, 0.4f),
                new Rectangle(0.2f, 0.1f, 0.2f, 0.4f), new Vector2(0.1f, 0.1f));
        resources.add(cameraController);

        Gdx.input.setInputProcessor(stage);

        if (debugRender) {
            debugRenderer = new Box2DDebugRenderer();
            tmpMatrix = new Matrix4();
        }
    }

    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }

    private StateBasedSprite createPlayerSprite() {
        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        GraphSprite player = graphSprites.createSprite(10f, "Animated");

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

        return new StateBasedSprite(player, new Vector2(-300, -200), new Vector2(256, 256), new Vector2(0.5f, 0.8f), "Idle", SpriteFaceDirection.Right, animationData);
    }

    private TiledSprite createGroundSprite() {
        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        GraphSprite ground = graphSprites.createSprite(9f, "Tiled");

        Texture groundTexture = new Texture(Gdx.files.classpath("image/MossyGround.png"));
        resources.add(groundTexture);

        graphSprites.setProperty(ground, "Tile Texture", new TextureRegion(groundTexture));
        graphSprites.setProperty(ground, "Tile Repeat", new Vector2(10, 1));

        return new TiledSprite(ground, new Vector2(0, -350), new Vector2(10 * 512, 128), new Vector2(0.5f, 0.5f));
    }

    private Skin createSkin() {
        return new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
    }

    private Stage createStage(Skin skin) {
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
    }

    @Override
    public void renderScene() {
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        stage.act(delta);

        playerControlSystem.update(delta);
        physicsSystem.update(delta);
        entitySystem.update(delta);

        cameraController.update(delta);
        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);

        if (debugRender) {
            tmpMatrix.set(camera.combined).scale(PhysicsSystem.PIXELS_TO_METERS, PhysicsSystem.PIXELS_TO_METERS, 0);
            debugRenderer.render(physicsSystem.getWorld(), tmpMatrix);
        }
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