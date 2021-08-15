package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.gempukku.libgdx.graph.plugin.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.shader.AttributeDefinition;
import com.gempukku.libgdx.graph.shader.ShaderContext;

import java.util.Map;

public class ParticlesDataContainer implements Disposable {
    private final static ParticleGenerator.ParticleGenerateInfo tempGenerateInfo = new ParticleGenerator.ParticleGenerateInfo();
    private final static ParticleUpdater.ParticleUpdateInfo tempUpdateInfo = new ParticleUpdater.ParticleUpdateInfo();

    private Object[] particleDataStorage;
    private float[] particlesData;
    private Mesh mesh;

    private Map<String, AttributeDefinition> additionalAttributes;
    private final int numberOfParticles;
    private final int numberOfFloatsInVertex;
    private int nextParticleIndex = 0;
    private float maxParticleDeath;

    private int firstDirtyParticle = Integer.MAX_VALUE;
    private int lastDirtyParticle = -1;

    private int uvOffset = -1;
    private int birthTimeOffset = -1;
    private int deathTimeOffset = -1;

    private IntMap<String> customAttributeOffsets = new IntMap<>();

    public ParticlesDataContainer(VertexAttributes vertexAttributes,
                                  Map<String, AttributeDefinition> additionalAttributes,
                                  int numberOfParticles, boolean storeParticleData) {
        this.additionalAttributes = additionalAttributes;
        this.numberOfParticles = numberOfParticles;
        this.numberOfFloatsInVertex = vertexAttributes.vertexSize / 4;
        initOffsets(vertexAttributes);
        initBuffers(vertexAttributes);
        if (storeParticleData)
            particleDataStorage = new Object[numberOfParticles];
    }

    private void initOffsets(VertexAttributes vertexAttributes) {
        uvOffset = getAliasOffset(vertexAttributes, ShaderProgram.TEXCOORD_ATTRIBUTE + 0);
        birthTimeOffset = getAliasOffset(vertexAttributes, "a_birthTime");
        deathTimeOffset = getAliasOffset(vertexAttributes, "a_deathTime");

        for (Map.Entry<String, AttributeDefinition> nameToAttribute : additionalAttributes.entrySet()) {
            String attributeName = nameToAttribute.getKey();
            String attributeAlias = nameToAttribute.getValue().getAlias();
            customAttributeOffsets.put(getAliasOffset(vertexAttributes, attributeAlias), attributeName);
        }
    }

    private int getAliasOffset(VertexAttributes vertexAttributes, String alias) {
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            if (vertexAttribute.alias.equals(alias))
                return vertexAttribute.offset / 4;
        }
        return -1;
    }

    private void initBuffers(VertexAttributes vertexAttributes) {
        int numberOfIndices = 6 * numberOfParticles;
        mesh = new Mesh(false, true, numberOfParticles * 4, numberOfIndices, vertexAttributes);

        int dataLength = numberOfParticles * 4 * numberOfFloatsInVertex;
        particlesData = new float[dataLength];
        if (uvOffset != -1) {
            for (int particle = 0; particle < numberOfParticles; particle++) {
                // Don't need to set UV for first vertex, as it's 0,0
                for (int vertex = 1; vertex < 4; vertex++) {
                    int dataIndex = getVertexIndex(particle, vertex);
                    particlesData[dataIndex + uvOffset] = vertex % 2;
                    particlesData[dataIndex + uvOffset + 1] = (float) (vertex / 2);
                }
            }
        }
        mesh.setVertices(particlesData);

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
        mesh.setIndices(indices);
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
        tempGenerateInfo.particleAttributes.clear();
        particleGenerator.generateParticle(tempGenerateInfo);
        if (particleDataStorage != null)
            particleDataStorage[nextParticleIndex] = tempGenerateInfo.particleData;

        float particleDeath = particleTime + tempGenerateInfo.lifeLength;
        for (int i = 0; i < 4; i++) {
            int vertexIndex = getVertexIndex(nextParticleIndex, i);
            if (birthTimeOffset != -1)
                particlesData[vertexIndex + birthTimeOffset] = particleTime;
            if (deathTimeOffset != -1)
                particlesData[vertexIndex + deathTimeOffset] = particleDeath;
        }

        for (IntMap.Entry<String> customAttributeEntry : customAttributeOffsets.entries()) {
            String attributeName = customAttributeEntry.value;
            Object attributeValue = tempGenerateInfo.particleAttributes.get(attributeName);
            AttributeDefinition attributeDefinition = additionalAttributes.get(attributeName);
            Object value = attributeDefinition.getFieldType().convert((attributeValue != null) ? attributeValue : attributeDefinition.getDefaultValue());
            for (int i = 0; i < 4; i++) {
                int vertexIndex = getVertexIndex(nextParticleIndex, i);
                setParticleValue(vertexIndex + customAttributeEntry.key, value);
            }
        }

        maxParticleDeath = Math.max(maxParticleDeath, particleDeath);

        firstDirtyParticle = Math.min(firstDirtyParticle, nextParticleIndex);
        lastDirtyParticle = Math.max(lastDirtyParticle, nextParticleIndex);

        nextParticleIndex = (nextParticleIndex + 1) % numberOfParticles;
    }

    private void setParticleValue(int offset, Object value) {
        if (value instanceof Number) {
            particlesData[offset + 0] = ((Number) value).floatValue();
        } else if (value instanceof Vector2) {
            particlesData[offset + 0] = ((Vector2) value).x;
            particlesData[offset + 1] = ((Vector2) value).y;
        } else if (value instanceof Vector3) {
            particlesData[offset + 0] = ((Vector3) value).x;
            particlesData[offset + 1] = ((Vector3) value).y;
            particlesData[offset + 2] = ((Vector3) value).z;
        } else if (value instanceof Color) {
            particlesData[offset + 0] = ((Color) value).r;
            particlesData[offset + 1] = ((Color) value).g;
            particlesData[offset + 2] = ((Color) value).b;
            particlesData[offset + 3] = ((Color) value).a;
        }
    }

    public void applyPendingChanges() {
        if (lastDirtyParticle >= 0) {
            if (firstDirtyParticle == lastDirtyParticle) {
                // Update all particles
                mesh.updateVertices(0, particlesData, 0, particlesData.length);
            } else if (firstDirtyParticle > lastDirtyParticle) {
                // Updates are wrapper around
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                mesh.updateVertices(firstData, particlesData, firstData, particlesData.length - firstData);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                mesh.updateVertices(0, particlesData, 0, lastData);
            } else {
                int firstData = getVertexIndex(firstDirtyParticle, 0);
                int lastData = getVertexIndex(lastDirtyParticle + 1, 0);
                mesh.updateVertices(firstData, particlesData, firstData, lastData - firstData);
            }
        }
    }

    public void render(ParticlesGraphShader graphShader, ShaderContext shaderContext, float currentTime) {
        if (currentTime < maxParticleDeath) {
            graphShader.renderParticles(shaderContext, mesh);
        }
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }

    public void update(ParticleUpdater particleUpdater, float currentTime, boolean accessToData) {
        for (int i = 0; i < numberOfParticles; i++) {
            int particleDataIndex = getVertexIndex(i, 0);
            if (currentTime < particlesData[particleDataIndex + 5]) {
                if (accessToData && particleDataStorage != null)
                    tempUpdateInfo.particleData = particleDataStorage[i];
                particleUpdater.updateParticle(tempUpdateInfo);

                if (accessToData && particleDataStorage != null)
                    particleDataStorage[i] = tempGenerateInfo.particleData;

                firstDirtyParticle = Math.min(firstDirtyParticle, i);
                lastDirtyParticle = Math.max(lastDirtyParticle, i);
            }
        }
    }
}
