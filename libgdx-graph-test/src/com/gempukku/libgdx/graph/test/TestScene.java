package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprite;
import com.gempukku.libgdx.graph.shader.sprite.GraphSprites;

import java.io.IOException;
import java.io.InputStream;

public class TestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Texture texture;
    private GraphSprite doctorSprite;
    private boolean doctorWalking = false;
    private boolean doctorRight = true;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        texture = new Texture(Gdx.files.classpath("image/professor_walk_cycle_no_hat.png"));

        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(pipelineRenderer.getGraphSprites());

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.near = 0.1f;
        camera.far = 100;
        camera.position.set(0, 0, 0);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0, -1f);
        camera.update();

        return camera;
    }

    private void createModels(GraphSprites graphSprites) {
        doctorSprite = graphSprites.createSprite(new Vector3(0, 0, -10f), "Default");

        graphSprites.setProperty(doctorSprite, "Size", new Vector2(100, 100));
        graphSprites.setProperty(doctorSprite, "Anchor", new Vector2(0.5f, 1));
        graphSprites.setProperty(doctorSprite, "Tiles Per Second", 12f);

        updateAnimated(doctorSprite, doctorWalking, doctorRight, 9);
    }

    private void updateAnimated(GraphSprite graphSprite, boolean walking, boolean right, int spriteCount) {
        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        float startX = walking ? 1f / spriteCount : 0f;
        float endX = walking ? 1f : 1f / spriteCount;
        float startY = right ? 0.75f : 0.25f;
        graphSprites.setProperty(graphSprite, "Texture", new TextureRegion(texture, startX, startY, endX, startY + 0.25f));
        graphSprites.setProperty(graphSprite, "Tile Count", new Vector2(walking ? (spriteCount - 1) : 1, 1));
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
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            doctorWalking = true;
            doctorRight = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            doctorWalking = true;
            doctorRight = false;
        } else {
            doctorWalking = false;
        }
        updateAnimated(doctorSprite, doctorWalking, doctorRight, 9);

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        texture.dispose();
        stage.dispose();
        skin.dispose();
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
        pipelineRenderer.setPipelineProperty("Stage", stage);
    }
}