package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.particles.generator.ParticleGenerator;

public class ParticlesDataContainer implements Disposable {
    private final static ParticleGenerator.ParticleGenerateInfo tempGenerateInfo = new ParticleGenerator.ParticleGenerateInfo();
    private final static ParticleUpdater.ParticleUpdateInfo tempUpdateInfo = new ParticleUpdater.ParticleUpdateInfo();
    private final static VertexAttributes vertexAttributes = new VertexAttributes(
            VertexAttribute.Position(),
            new VertexAttribute(512, 1, "a_seed"),
            new VertexAttribute(1024, 1, "a_birthTime"),
            new VertexAttribute(2048, 1, "a_deathTime"),
            VertexAttribute.TexCoords(0)
    );
    private final static int numberOfFloatsInVertex = 3 + 1 + 1 + 1 + 2;

    private Object[] particleDataStorage;
    private float[] particlesData;
    private VertexBufferObject vbo;
    private IndexBufferObject ibo;

    private final int numberOfParticles;
    private int nextParticleIndex = 0;
    private float maxParticleDeath;

    private int firstDirtyParticle = Integer.MAX_VALUE;
    private int lastDirtyParticle = -1;

    public ParticlesDataContainer(int numberOfParticles, boolean storeParticleData) {
        this.numberOfParticles = numberOfParticles;
        initBuffers();
        if (storeParticleData)
            particleDataStorage = new Object[numberOfParticles];
    }

    private void initBuffers() {
        vbo = new VertexBufferObject(false, numberOfParticles * 4, vertexAttributes);

        int dataLength = numberOfParticles * 4 * numberOfFloatsInVertex;
        particlesData = new float[dataLength];
        for (int particle = 0; particle < numberOfParticles; particle++) {
            for (int vertex = 0; vertex < 4; vertex++) {
                int dataIndex = getVertexIndex(particle, vertex);
                particlesData[dataIndex + 6] = vertex % 2;
                particlesData[dataIndex + 7] = (float) (vertex / 2);
            }
        }
        vbo.setVertices(particlesData, 0, dataLength);

        int numberOfIndices = 6 * numberOfParticles;
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

    private int getVertexIndex(int particleIndex, int vertexInParticle) {
        return ((particleIndex * 4) + vertexInParticle) * numberOfFloatsInVertex;
    }

    public int getNumberOfParticles() {
        return numberOfParticles;
    }

    public int getRemainingCapacity() {
        return numberOfParticles - nextParticleIndex;
    }

    public void generateParticle(float particleTime, ParticleGenerator particleGenerator) {
        particleGenerator.generateParticle(tempGenerateInfo);
        if (particleDataStorage != null)
            particleDataStorage[nextParticleIndex] = tempGenerateInfo.particleData;

        float particleDeath = particleTime + tempGenerateInfo.lifeLength;
        for (int i = 0; i < 4; i++) {
            int vertexIndex = getVertexIndex(nextParticleIndex, i);
            particlesData[vertexIndex + 0] = tempGenerateInfo.location.x;
            particlesData[vertexIndex + 1] = tempGenerateInfo.location.y;
            particlesData[vertexIndex + 2] = tempGenerateInfo.location.z;
            particlesData[vertexIndex + 3] = tempGenerateInfo.seed;
            particlesData[vertexIndex + 4] = particleTime;
            particlesData[vertexIndex + 5] = particleDeath;
        }
        maxParticleDeath = Math.max(maxParticleDeath, particleDeath);

        firstDirtyParticle = Math.min(firstDirtyParticle, nextParticleIndex);
        lastDirtyParticle = Math.max(lastDirtyParticle, nextParticleIndex);

        nextParticleIndex = (nextParticleIndex + 1) % numberOfParticles;
    }

    public void applyPendingChanges() {
        if (lastDirtyParticle >= 0) {
            if (firstDirtyParticle == lastDirtyParticle) {
                // Update all particles
                vbo.updateVertices(0, particlesData, 0, particlesData.length);
            } else if (firstDirtyParticle > lastDirtyParticle) {
                // Updates are wrapper around
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                vbo.updateVertices(firstData, particlesData, firstData, particlesData.length - firstData);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                vbo.updateVertices(0, particlesData, 0, lastData);
            } else {
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                vbo.updateVertices(firstData, particlesData, firstData, lastData - firstData);
            }
        }
    }

    public void render(ParticlesGraphShader graphShader, ShaderContext shaderContext, float currentTime) {
        if (currentTime < maxParticleDeath) {
            graphShader.renderParticles(shaderContext, vbo, ibo);
        }
    }

    @Override
    public void dispose() {
        vbo.dispose();
        ibo.dispose();
    }

    public void update(ParticleUpdater particleUpdater, float currentTime) {
        for (int i = 0; i < numberOfParticles; i++) {
            int particleDataIndex = getVertexIndex(i, 0);
            if (currentTime < particlesData[particleDataIndex + 5]) {
                tempUpdateInfo.location.set(particlesData[particleDataIndex + 0], particlesData[particleDataIndex + 1], particlesData[particleDataIndex + 2]);
                tempUpdateInfo.seed = particlesData[particleDataIndex + 3];
                particleUpdater.updateParticle(tempUpdateInfo);

                if (particleDataStorage != null)
                    particleDataStorage[i] = tempGenerateInfo.particleData;

                for (int vertex = 0; vertex < 4; vertex++) {
                    int vertexIndex = getVertexIndex(i, vertex);
                    particlesData[vertexIndex + 0] = tempUpdateInfo.location.x;
                    particlesData[vertexIndex + 1] = tempUpdateInfo.location.x;
                    particlesData[vertexIndex + 2] = tempUpdateInfo.location.x;
                    particlesData[vertexIndex + 3] = tempUpdateInfo.seed;
                }

                firstDirtyParticle = Math.min(firstDirtyParticle, i);
                lastDirtyParticle = Math.max(lastDirtyParticle, i);
            }
        }
    }
}
