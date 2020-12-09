package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class GraphParticleEffect implements Disposable {
    private final static int MAX_NUMBER_OF_VERTICES = 32768;
    private final static int MAX_NUMBER_OF_PARTICLES = (32768 - 1) / 4;

    private final static ParticleGenerator.ParticleInfo tempInfo = new ParticleGenerator.ParticleInfo();
    private final static float[] tempData = new float[6];
    private final static VertexAttributes vertexAttributes = new VertexAttributes(
            VertexAttribute.Position(),
            new VertexAttribute(512, 1, "a_seed"),
            new VertexAttribute(1024, 1, "a_birthTime"),
            new VertexAttribute(2048, 1, "a_deathTime"),
            VertexAttribute.TexCoords(0)
    );
    private final static int numberOfFloatsInVertex = 3 + 1 + 1 + 1 + 2;

    private String tag;
    private ParticleEffectConfiguration particleEffectConfiguration;
    private ParticleGenerator particleGenerator;
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();
    private boolean running = false;

    private VertexBufferObject vbo;
    private IndexBufferObject ibo;
    private int nextParticleIndex = 0;

    public GraphParticleEffect(String tag, ParticleEffectConfiguration particleEffectConfiguration, ParticleGenerator particleGenerator) {
        this.tag = tag;
        this.particleEffectConfiguration = particleEffectConfiguration;
        this.particleGenerator = particleGenerator;

        initializeBuffers(particleEffectConfiguration);
    }

    public PropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    public String getTag() {
        return tag;
    }

    private int getVertexIndex(int particleIndex, int vertexInParticle) {
        return ((particleIndex * 4) + vertexInParticle) * numberOfFloatsInVertex;
    }

    private void initializeBuffers(ParticleEffectConfiguration particleEffectConfiguration) {
        int maxNumberOfParticles = particleEffectConfiguration.getMaxNumberOfParticles();
        vbo = new VertexBufferObject(false, maxNumberOfParticles * 4, vertexAttributes);

        int dataLength = maxNumberOfParticles * 4 * numberOfFloatsInVertex;
        float[] vertexData = new float[dataLength];
        for (int particle = 0; particle < maxNumberOfParticles; particle++) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int dataIndex = getVertexIndex(particle, vertex);
                vertexData[dataIndex + 6] = vertex % 2;
                vertexData[dataIndex + 7] = (float) (vertex / 2);
            }
        }
        vbo.setVertices(vertexData, 0, dataLength);

        int numberOfIndices = 6 * maxNumberOfParticles;
        ibo = new IndexBufferObject(false, numberOfIndices);
        short[] indices = new short[numberOfIndices];
        int vertexIndex = 0;
        for (int i = 0; i < numberOfIndices; i += 6) {
            indices[i + 0] = (short) (vertexIndex * 4 + 0);
            indices[i + 1] = (short) (vertexIndex * 4 + 2);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            indices[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
        ibo.setIndices(indices, 0, indices.length);
    }

    public void generateParticles(TimeProvider timeProvider) {
        if (running) {
            for (int i = 0; i < 10; i++) {
                particleGenerator.generateParticle(tempInfo);
                tempData[0] = tempInfo.location.x;
                tempData[1] = tempInfo.location.y;
                tempData[2] = tempInfo.location.z;
                tempData[3] = tempInfo.seed;
                tempData[4] = timeProvider.getTime();
                tempData[5] = tempData[4] + tempInfo.lifeLength;

                vbo.updateVertices(getVertexIndex(nextParticleIndex, 0), tempData, 0, tempData.length);
                vbo.updateVertices(getVertexIndex(nextParticleIndex, 1), tempData, 0, tempData.length);
                vbo.updateVertices(getVertexIndex(nextParticleIndex, 2), tempData, 0, tempData.length);
                vbo.updateVertices(getVertexIndex(nextParticleIndex, 3), tempData, 0, tempData.length);

                nextParticleIndex++;
                if (nextParticleIndex == particleEffectConfiguration.getMaxNumberOfParticles())
                    nextParticleIndex = 0;
            }
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
