package com.gempukku.libgdx.graph.ui.shader.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
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
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.particles.GraphParticleEffect;
import com.gempukku.libgdx.graph.shader.particles.ParticleEffectConfiguration;
import com.gempukku.libgdx.graph.shader.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.shader.particles.generator.PointParticleGenerator;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ParticlesShaderPreviewWidget extends Widget implements Disposable {
    private boolean shaderInitialized;
    private boolean running;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private ParticlesGraphShader graphShader;
    private RenderContext renderContext;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private GraphShaderEnvironment graphShaderEnvironment;
    private ShaderContextImpl shaderContext;
    private GraphParticleEffect particleEffect;

    public ParticlesShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-1f, 1f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 1f, 0f);
        camera.update();

        graphShaderEnvironment = new GraphShaderEnvironment();
        graphShaderEnvironment.setAmbientColor(Color.DARK_GRAY);
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -2f, 1f, 1f, 2f);
        graphShaderEnvironment.addPointLight(pointLight);

        shaderContext = new ShaderContextImpl();
        shaderContext.setGraphShaderEnvironment(graphShaderEnvironment);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);
    }

    public void setCameraDistance(float distance) {
        camera.position.set(-distance, distance, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (particleEffect != null) {
            if (running)
                particleEffect.start();
            else
                particleEffect.stop();
        }
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

    private void createShader(final Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        try {
            timeKeeper = new DefaultTimeKeeper();
            graphShader = GraphShaderBuilder.buildParticlesShader(WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            createModel(graphShader.getVertexAttributes());

            shaderContext.setGraphShaderEnvironment(graphShaderEnvironment);
            shaderContext.setTimeProvider(timeKeeper);
            shaderContext.setPropertyContainer(
                    new PropertyContainer() {
                        @Override
                        public Object getValue(String name) {
                            for (GraphProperty<ShaderFieldType> property : graph.getProperties()) {
                                if (property.getName().equals(name)) {
                                    ShaderFieldType propertyType = property.getType();
                                    return propertyType.convertFromJson(property.getData());
                                }
                            }

                            return null;
                        }
                    }
            );

            shaderInitialized = true;
        } catch (Exception exp) {
            exp.printStackTrace();
            if (graphShader != null)
                graphShader.dispose();
        }
    }

    private void createModel(Array<VertexAttribute> vertexAttributeArray) {
        VertexAttribute[] vAttributes = new VertexAttribute[vertexAttributeArray.size];
        for (int i = 0; i < vAttributes.length; i++) {
            vAttributes[i] = vertexAttributeArray.get(i);
        }

        particleEffect = new GraphParticleEffect("tag", new ParticleEffectConfiguration(
                graphShader.getMaxNumberOfParticles(), graphShader.getInitialParticles(), 1f / graphShader.getPerSecondParticles()),
                new PointParticleGenerator(10), false);
        if (running)
            particleEffect.start();
    }

    private void destroyShader() {
        particleEffect.dispose();
        particleEffect = null;
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

            particleEffect.generateParticles(shaderContext.getTimeProvider());

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
                particleEffect.render(graphShader, shaderContext);
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
