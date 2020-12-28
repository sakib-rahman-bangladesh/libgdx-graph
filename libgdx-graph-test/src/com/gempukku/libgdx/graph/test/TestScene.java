package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private Texture texture;
    private AnimatedSprite doctorSprite;

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

    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }

    private void createModels(GraphSprites graphSprites) {
        GraphSprite doctor = graphSprites.createSprite(10f, "Animated");

        graphSprites.setProperty(doctor, "Position", new Vector2(0, 0));
        graphSprites.setProperty(doctor, "Size", new Vector2(200, 200));
        graphSprites.setProperty(doctor, "Anchor", new Vector2(0.5f, 1));
        graphSprites.setProperty(doctor, "Tiles Per Second", 12f);

        doctorSprite = new AnimatedSprite(doctor, new Vector2(0, 0), WalkDirection.Right, 9);
        updateAnimated(doctorSprite);
    }

    private void updateAnimated(AnimatedSprite animatedSprite) {
        GraphSprites graphSprites = pipelineRenderer.getGraphSprites();

        GraphSprite sprite = animatedSprite.getGraphSprite();
        int spriteCount = animatedSprite.getSpriteCount();
        boolean walking = animatedSprite.isWalking();
        WalkDirection walkDirection = animatedSprite.getWalkDirection();

        float startX = walking ? 1f / spriteCount : 0f;
        float endX = walking ? 1f : 1f / spriteCount;
        float startY = 0f;
        if (walkDirection == WalkDirection.Left)
            startY = 0.25f;
        else if (walkDirection == WalkDirection.Down)
            startY = 0.5f;
        else if (walkDirection == WalkDirection.Right)
            startY = 0.75f;

        graphSprites.setProperty(sprite, "Animated Texture", new TextureRegion(texture, startX, startY, endX, startY + 0.25f));
        graphSprites.setProperty(sprite, "Tile Count", new Vector2(walking ? (spriteCount - 1) : 1, 1));
        graphSprites.setProperty(sprite, "Position", animatedSprite.getPosition());
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
        float delta = Math.min(0.03f, Gdx.graphics.getDeltaTime());
        stage.act(delta);

        boolean update;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            update = doctorSprite.walkDirection(delta, 100f, WalkDirection.Right);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            update = doctorSprite.walkDirection(delta, 100f, WalkDirection.Left);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            update = doctorSprite.walkDirection(delta, 100f, WalkDirection.Up);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            update = doctorSprite.walkDirection(delta, 100f, WalkDirection.Down);
        } else {
            update = doctorSprite.stopWalking();
        }
        if (update)
            updateAnimated(doctorSprite);

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

    private enum WalkDirection {
        Left(-1, 0), Right(1, 0), Up(0, 1), Down(0, -1);

        private int x;
        private int y;

        WalkDirection(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private class AnimatedSprite {
        private GraphSprite graphSprite;
        private int spriteCount;
        private boolean walking = false;
        private WalkDirection walkDirection;
        private Vector2 position = new Vector2();

        public AnimatedSprite(GraphSprite graphSprite, Vector2 position, WalkDirection walkDirection, int spriteCount) {
            this.graphSprite = graphSprite;
            this.position.set(position);
            this.walkDirection = walkDirection;
            this.spriteCount = spriteCount;
        }

        public boolean stopWalking() {
            boolean needsUpdate = walking;
            walking = false;
            return needsUpdate;
        }

        public boolean walkDirection(float delta, float speed, WalkDirection walkDirection) {
            position.add(walkDirection.getX() * delta * speed, walkDirection.getY() * delta * speed);
            boolean needsUpdate = !walking || this.walkDirection != walkDirection;
            walking = true;
            this.walkDirection = walkDirection;
            return needsUpdate;
        }

        public GraphSprite getGraphSprite() {
            return graphSprite;
        }

        public int getSpriteCount() {
            return spriteCount;
        }

        public boolean isWalking() {
            return walking;
        }

        public WalkDirection getWalkDirection() {
            return walkDirection;
        }

        public Vector2 getPosition() {
            return position;
        }
    }
}