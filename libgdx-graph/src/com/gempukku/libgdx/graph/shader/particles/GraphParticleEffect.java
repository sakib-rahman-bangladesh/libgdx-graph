package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffect implements Disposable {
    private final static int MAX_NUMBER_OF_VERTICES = 32768;
    private final static int MAX_NUMBER_OF_PARTICLES = (32768 - 1) / 4;

    private final static ParticleGenerator.ParticleInfo tempInfo = new ParticleGenerator.ParticleInfo();
    private final static float[] tempData = new float[6];

    private ParticleEffectConfiguration particleEffectConfiguration;
    private ParticleGenerator particleGenerator;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();
    private boolean running = false;

    private VertexBufferObject vbo;
    private IndexBufferObject ibo;
    private short restartIndex;
    private int nextParticleIndex = 0;

    public GraphParticleEffect(ParticleEffectConfiguration particleEffectConfiguration, ParticleGenerator particleGenerator) {
        this.particleEffectConfiguration = particleEffectConfiguration;
        this.particleGenerator = particleGenerator;

        initializeBuffers(particleEffectConfiguration);
    }

    private void initializeBuffers(ParticleEffectConfiguration particleEffectConfiguration) {
        int maxNumberOfParticles = particleEffectConfiguration.getMaxNumberOfParticles();
        vbo = new VertexBufferObject(false, maxNumberOfParticles * 4,
                VertexAttribute.Position(),
                new VertexAttribute(512, 1, "a_seed"),
                new VertexAttribute(1024, 1, "a_birthTime"),
                new VertexAttribute(2048, 1, "a_deathTime"),
                VertexAttribute.TexCoords(0));

        int numberOfFloatsInVertex = 3 + 1 + 1 + 1 + 2;
        int dataLength = maxNumberOfParticles * 4 * numberOfFloatsInVertex;
        vbo.setVertices(new float[dataLength], 0, dataLength);
        restartIndex = (short) dataLength;

        int numberOfIndices = 5 * maxNumberOfParticles - 1;
        ibo = new IndexBufferObject(false, numberOfIndices);
        short[] indices = new short[numberOfIndices];
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 3;
        int vertexIndex = 1;
        for (int i = 4; i < numberOfIndices; i += 5) {
            indices[i] = restartIndex;
            indices[i + 1] = (short) (vertexIndex * 4);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            vertexIndex++;
        }
    }

    public void generateParticles(TimeProvider timeProvider) {
        if (running) {
            particleGenerator.generateParticle(tempInfo);
            tempData[0] = tempInfo.location.x;
            tempData[1] = tempInfo.location.y;
            tempData[2] = tempInfo.location.z;
            tempData[3] = tempInfo.seed;
            tempData[4] = timeProvider.getTime();
            tempData[5] = tempData[4] + tempInfo.lifeLength;
            vbo.updateVertices(8 * nextParticleIndex, tempData, 0, tempData.length);
            nextParticleIndex++;
            if (nextParticleIndex == particleEffectConfiguration.getMaxNumberOfParticles())
                nextParticleIndex = 0;
        }
    }

    public void render(GraphShader graphShader, ShaderContext shaderContext) {
        graphShader.renderParticles(shaderContext, vbo, ibo);
    }

    public void start() {
        if (running)
            throw new IllegalStateException("Already started");
        running = true;
    }

    public void update(ParticleUpdater particleUpdater) {
        // TODO
    }

    public void stop() {
        if (!running)
            throw new IllegalStateException("Not started");
        running = false;
    }

    public void setProperty(String name, Object value) {
        propertyContainer.setValue(name, value);
    }

    public void unsetProperty(String name) {
        propertyContainer.remove(name);
    }

    public Object getProperty(String name) {
        return propertyContainer.getValue(name);
    }

    @Override
    public void dispose() {
        vbo.dispose();
        ibo.dispose();
    }
}
