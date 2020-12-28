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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
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
    private Array<Disposable> resources = new Array<>();
    private PipelineRenderer pipelineRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private AnimatedSprite doctorSprite;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();

        stage = createStage();
        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        resources.add(pipelineRenderer);

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

        graphSprites.setProperty(doctor, "Anchor", new Vector2(0.5f, 0.8f));

        Texture idleTexture = new Texture(Gdx.files.classpath("image/BlueWizardIdle.png"));
        resources.add(idleTexture);
        Texture walkTexture = new Texture(Gdx.files.classpath("image/BlueWizardWalk.png"));
        resources.add(walkTexture);
        Texture jumpTexture = new Texture(Gdx.files.classpath("image/BlueWizardJump.png"));
        resources.add(jumpTexture);

        ObjectMap<String, AnimationData> animationData = new ObjectMap<>();
        animationData.put("Idle", new AnimationData(new TextureRegion(idleTexture), 20, 1, 20f, true));
        animationData.put("Walk", new AnimationData(new TextureRegion(walkTexture), 5, 4, 20f, true));
        animationData.put("Jump", new AnimationData(new TextureRegion(jumpTexture), 8, 1, 20f, false));

        doctorSprite = new AnimatedSprite(doctor, new Vector2(0, 0), new Vector2(300, 300), "Idle", FaceDirection.Right, animationData);
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

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            doctorSprite.addPosition(delta * 100f, 0);
            doctorSprite.setFaceDirection(FaceDirection.Right);
            doctorSprite.setState("Walk");
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            doctorSprite.addPosition(-delta * 100f, 0);
            doctorSprite.setFaceDirection(FaceDirection.Left);
            doctorSprite.setState("Walk");
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            doctorSprite.setState("Jump");
        } else {
            doctorSprite.setState("Idle");
        }
        if (doctorSprite.isDirty())
            doctorSprite.updateSprite(pipelineRenderer);

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

    private enum FaceDirection {
        Left(-1, 0), Right(1, 0);

        private int x;
        private int y;

        FaceDirection(int x, int y) {
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

    private class AnimationData {
        private TextureRegion sprites;
        private int spriteWidth;
        private int spriteHeight;
        private float speed;
        private boolean looping;

        public AnimationData(TextureRegion sprites, int spriteWidth, int spriteHeight, float speed, boolean looping) {
            this.sprites = sprites;
            this.spriteWidth = spriteWidth;
            this.spriteHeight = spriteHeight;
            this.speed = speed;
            this.looping = looping;
        }
    }

    private class AnimatedSprite {
        private GraphSprite graphSprite;
        private Vector2 position = new Vector2();
        private Vector2 size = new Vector2();
        private Vector2 mergedSize = new Vector2();
        private FaceDirection faceDirection;
        private String state;
        private ObjectMap<String, AnimationData> statesData;
        private boolean dirty = true;
        private boolean animationDirty = true;

        public AnimatedSprite(GraphSprite graphSprite, Vector2 position, Vector2 size,
                              String state, FaceDirection faceDirection,
                              ObjectMap<String, AnimationData> statesData) {
            this.graphSprite = graphSprite;
            this.position.set(position);
            this.size.set(size);
            this.state = state;
            this.faceDirection = faceDirection;
            this.statesData = statesData;
        }

        public void setState(String state) {
            if (!statesData.containsKey(state))
                throw new IllegalArgumentException("Undefined state for the sprite");
            if (!this.state.equals(state)) {
                animationDirty = true;
                dirty = true;
                this.state = state;
            }
        }

        public void setFaceDirection(FaceDirection faceDirection) {
            if (this.faceDirection != faceDirection) {
                dirty = true;
                this.faceDirection = faceDirection;
            }
        }

        public void addPosition(float x, float y) {
            if (x != 0 || y != 0) {
                this.position.add(x, y);
                dirty = true;
            }
        }

        public boolean isDirty() {
            return dirty;
        }

        public void updateSprite(PipelineRenderer pipelineRenderer) {
            if (dirty) {
                AnimationData animationData = statesData.get(state);

                GraphSprites graphSprites = pipelineRenderer.getGraphSprites();
                graphSprites.setProperty(graphSprite, "Position", position);
                graphSprites.setProperty(graphSprite, "Size", mergedSize.set(faceDirection.x, 1).scl(size));
                if (animationDirty) {
                    graphSprites.setProperty(graphSprite, "Animated Texture", animationData.sprites);
                    graphSprites.setProperty(graphSprite, "Animation Speed", animationData.speed);
                    graphSprites.setProperty(graphSprite, "Animation Looping", animationData.looping ? 1f : 0f);
                    graphSprites.setProperty(graphSprite, "Tiles Width", animationData.spriteWidth);
                    graphSprites.setProperty(graphSprite, "Tiles Height", animationData.spriteHeight);
                    graphSprites.setProperty(graphSprite, "Animation Start", pipelineRenderer.getTime());
                }

                dirty = false;
                animationDirty = false;
            }
        }
    }
}