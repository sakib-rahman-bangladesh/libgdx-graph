package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.CustomRenderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.test.WhitePixel;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.time.TimeKeeper;

import java.io.IOException;
import java.io.InputStream;

public class Episode4Scene implements LibgdxGraphTestScene {
    private Stage stage;
    private Skin skin;

    private PipelineRenderer pipelineRenderer;
    private Array<RenderableProvider> renderableProviders = new Array<>();
    private Model sphereModel;
    private Environment environment;
    private Camera camera1;
    private Camera camera2;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private ModelBatch modelBatch;

    @Override
    public void initializeScene() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        stage = constructStage();

        createModels();

        environment = createEnvironment();

        camera1 = createCamera();
        camera2 = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        modelBatch = new ModelBatch();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(3f, 3f, 0f);
        camera.lookAt(0, 0, 0);
        camera.update();
        return camera;
    }

    private Environment createEnvironment() {
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        return environment;
    }

    private void createModels() {
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture), ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.5f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        renderableProviders.add(new ModelInstance(sphereModel));
    }

    private Stage constructStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider angle = new Slider(0, 360, 1, false, skin);
        angle.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        float angleValue = angle.getValue();
                        float x = 3f * MathUtils.sinDeg(angleValue);
                        float z = 3f * MathUtils.cosDeg(angleValue);

                        camera1.position.set(x, 3f, z);
                        camera1.up.set(0, 1f, 0f);
                        camera1.lookAt(0, 0, 0);
                        camera1.update();
                    }
                });

        Table tbl = new Table();
        tbl.add(new Label("Camera angle", skin));
        tbl.add(angle).row();
        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera1.viewportWidth = width / 2f;
        camera1.viewportHeight = height;
        camera1.update();
        camera2.viewportWidth = width / 2f;
        camera2.viewportHeight = height;
        camera2.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        renderableProviders.clear();
        modelBatch.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        skin.dispose();
        stage.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode4.json").read();
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
        pipelineRenderer.setPipelineProperty("Stage", stage);
        pipelineRenderer.setPipelineProperty("Callback1", new ModelsRenderCallback(camera1));
        pipelineRenderer.setPipelineProperty("Callback2", new ModelsRenderCallback(camera2));
    }

    private class ModelsRenderCallback implements CustomRenderCallback {
        private Camera camera;

        public ModelsRenderCallback(Camera camera) {
            this.camera = camera;
        }

        @Override
        public void renderCallback(RenderPipeline renderPipeline, PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements) {
            RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

            currentBuffer.beginColor();

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

            modelBatch.begin(camera);
            modelBatch.render(renderableProviders, environment);
            modelBatch.end();

            currentBuffer.endColor();
        }
    }
}