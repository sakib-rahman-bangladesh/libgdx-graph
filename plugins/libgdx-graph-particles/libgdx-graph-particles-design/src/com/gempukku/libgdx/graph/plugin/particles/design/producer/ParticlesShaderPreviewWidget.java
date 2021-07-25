package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.plugin.particles.GraphParticleEffectImpl;
import com.gempukku.libgdx.graph.plugin.particles.ParticleEffectConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.plugin.particles.generator.LineParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.generator.PointParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.generator.SphereParticleGenerator;
import com.gempukku.libgdx.graph.plugin.particles.generator.SphereSurfaceParticleGenerator;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class ParticlesShaderPreviewWidget extends Widget implements Disposable {
    public enum ShaderPreviewModel {
        Point, SphereSurface, Sphere, Line
    }

    private boolean shaderInitialized;
    private boolean running;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private ParticlesGraphShader graphShader;
    private RenderContext renderContext;

    private Camera camera;
    private DefaultTimeKeeper timeKeeper;
    private Lighting3DEnvironment graphShaderEnvironment;
    private ShaderContextImpl shaderContext;
    private GraphParticleEffectImpl particleEffect;
    private ShaderPreviewModel model = ShaderPreviewModel.Point;
    private float lifetime = 3f;

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

        graphShaderEnvironment = new Lighting3DEnvironment();
        graphShaderEnvironment.setAmbientColor(Color.DARK_GRAY);
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -2f, 1f, 1f, 2f);
        graphShaderEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderEnvironment);

        PluginPrivateDataSource dataSource = new PluginPrivateDataSource() {
            @Override
            public <T> T getPrivatePluginData(Class<T> clazz) {
                if (clazz == Lighting3DPrivateData.class)
                    return (T) data;
                return null;
            }
        };

        shaderContext = new ShaderContextImpl(dataSource);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
        if (particleEffect != null) {
            particleEffect.setParticleGenerator(createGenerator());
        }
    }

    public void setModel(ShaderPreviewModel model) {
        this.model = model;
        if (particleEffect != null) {
            particleEffect.setParticleGenerator(createGenerator());
        }
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

    private void createShader(final Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
        try {
            timeKeeper = new DefaultTimeKeeper();
            graphShader = GraphShaderBuilder.buildParticlesShader(WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            createModel(graphShader.getVertexAttributes());

            shaderContext.setTimeProvider(timeKeeper);
            shaderContext.setPropertyContainer(
                    new PropertyContainer() {
                        @Override
                        public Object getValue(String name) {
                            for (GraphProperty property : graph.getProperties()) {
                                if (property.getName().equals(name)) {
                                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                                    Object value = propertyType.convertFromJson(property.getData());
                                    if (propertyType.isTexture()) {
                                        if (value != null) {
                                            try {
                                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                                graphShader.addManagedResource(texture);
                                                return new TextureRegion(texture);
                                            } catch (Exception exp) {

                                            }
                                        }
                                        return WhitePixel.sharedInstance.textureRegion;
                                    } else {
                                        return value;
                                    }
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

    private void createModel(VertexAttributes vertexAttributes) {
        particleEffect = new GraphParticleEffectImpl("tag", new ParticleEffectConfiguration(
                vertexAttributes, graphShader.getMaxNumberOfParticles(), graphShader.getInitialParticles(), 1f / graphShader.getPerSecondParticles()),
                createGenerator(), false);
        if (running)
            particleEffect.start();
    }

    private ParticleGenerator createGenerator() {
        switch (model) {
            case Point:
                return new PointParticleGenerator(lifetime);
            case Sphere:
                return new SphereParticleGenerator(lifetime);
            case SphereSurface:
                return new SphereSurfaceParticleGenerator(lifetime);
            case Line: {
                LineParticleGenerator particleGenerator = new LineParticleGenerator(lifetime);
                particleGenerator.getPoint1().set(0, 0, -1f);
                particleGenerator.getPoint2().set(0, 0, 1f);
                return particleGenerator;
            }

        }
        return null;
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


            timeKeeper.updateTime(Gdx.graphics.getDeltaTime());
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            try {
                particleEffect.generateParticles(shaderContext.getTimeProvider());

                frameBuffer.begin();
                camera.viewportWidth = width;
                camera.viewportHeight = height;
                camera.update();

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

    public void graphChanged(boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
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
