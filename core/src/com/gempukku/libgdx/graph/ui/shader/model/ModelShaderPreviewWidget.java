package com.gempukku.libgdx.graph.ui.shader.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelShaderContextImpl;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.model.ModelGraphShader;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.impl.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.model.impl.ModelBasedGraphShaderModel;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ModelShaderPreviewWidget extends Widget implements Disposable {
    public enum ShaderPreviewModel {
        Sphere, Rectangle
    }

    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private ModelGraphShader graphShader;
    private RenderContext renderContext;

    private Model rectangleModel;
    private ModelBasedGraphShaderModel rectangleShaderModel;
    private GraphShaderModelInstance rectangleModelInstance;
    private Model sphereModel;
    private ModelBasedGraphShaderModel sphereShaderModel;
    private GraphShaderModelInstance sphereModelInstance;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private GraphShaderEnvironment graphShaderEnvironment;
    private ModelShaderContextImpl shaderContext;
    private ShaderPreviewModel model = ShaderPreviewModel.Sphere;

    public ModelShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.9f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        graphShaderEnvironment = new GraphShaderEnvironment();
        graphShaderEnvironment.setAmbientColor(Color.DARK_GRAY);
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -2f, 1f, 1f, 2f);
        graphShaderEnvironment.addPointLight(pointLight);

        shaderContext = new ModelShaderContextImpl();
        shaderContext.setGraphShaderEnvironment(graphShaderEnvironment);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);
    }

    public void setModel(ShaderPreviewModel model) {
        this.model = model;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null && shaderInitialized) {
            destroyShader();
        }
    }

    @Override
    public float getPrefWidth() {
        return width;
    }

    @Override
    public float getPrefHeight() {
        return height;
    }

    private void createShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        try {
            timeKeeper = new DefaultTimeKeeper();
            graphShader = GraphShaderBuilder.buildModelShader(WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            createModel(graphShader.getVertexAttributes());

            shaderContext.setGraphShaderEnvironment(graphShaderEnvironment);
            shaderContext.setTimeProvider(timeKeeper);

            shaderInitialized = true;
        } catch (Exception exp) {
            exp.printStackTrace();
            if (graphShader != null)
                graphShader.dispose();
        }
    }

    private void createModel(Array<VertexAttribute> vertexAttributeArray) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();

        VertexAttribute[] vAttributes = new VertexAttribute[vertexAttributeArray.size];
        for (int i = 0; i < vAttributes.length; i++) {
            vAttributes[i] = vertexAttributeArray.get(i);
        }

        VertexAttributes vertexAttributes = new VertexAttributes(vAttributes);

        rectangleModel = modelBuilder.createRect(
                0, -0.5f, -0.5f,
                0, -0.5f, 0.5f,
                0, 0.5f, 0.5f,
                0, 0.5f, -0.5f,
                1, 0, 0,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        rectangleShaderModel = new ModelBasedGraphShaderModel("rectangle", rectangleModel, vertexAttributes);
        rectangleModelInstance = rectangleShaderModel.createInstance("rectangleInstance", ModelInstanceOptimizationHints.unoptimized);

        float sphereDiameter = 0.8f;
        sphereModel = modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        sphereShaderModel = new ModelBasedGraphShaderModel("sphere", sphereModel, vertexAttributes);
        sphereModelInstance = sphereShaderModel.createInstance("sphereInstance", ModelInstanceOptimizationHints.unoptimized);
    }

    private void destroyShader() {
        sphereModel.dispose();
        sphereShaderModel.dispose();
        rectangleModel.dispose();
        rectangleShaderModel.dispose();
        frameBuffer.dispose();
        frameBuffer = null;
        graphShader.dispose();
        shaderInitialized = false;
    }

    @Override
    public void dispose() {
        if (shaderInitialized)
            destroyShader();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (frameBuffer != null) {
            batch.end();

            timeKeeper.updateTime(Gdx.graphics.getDeltaTime());
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            try {
                frameBuffer.begin();
                camera.viewportWidth = width;
                camera.viewportHeight = height;
                camera.update();

                graphShader.setTimeProvider(timeKeeper);

                renderContext.begin();
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                graphShader.begin(shaderContext, renderContext);
                if (model == ShaderPreviewModel.Sphere)
                    graphShader.render(shaderContext, sphereModelInstance);
                else if (model == ShaderPreviewModel.Rectangle)
                    graphShader.render(shaderContext, rectangleModelInstance);
                graphShader.end();
                frameBuffer.end();
                renderContext.end();
            } catch (Exception exp) {
                // Ignore
                exp.printStackTrace();
            } finally {
                if (ScissorStack.peekScissors() != null)
                    Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            }

            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + height, width, -height);
        }
    }

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        if (hasErrors && shaderInitialized) {
            destroyShader();
        } else if (!hasErrors && !shaderInitialized) {
            createShader(graph);
        } else if (!hasErrors && shaderInitialized) {
            destroyShader();
            createShader(graph);
        }
    }
}
