package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.graph.plugin.models.impl.IGraphModelInstance;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelDataProducer;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelInstanceDataImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderContextImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ModelGraphShader extends GraphShader {
    private final IntArray tempArray = new IntArray();
    private Mesh currentMesh;
    private ModelInstanceDataImpl modelInstanceData = new ModelInstanceDataImpl();

    public ModelGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public void render(ModelShaderContextImpl shaderContext, IGraphModelInstance graphModelInstance) {
        shaderContext.setLocalPropertyContainer(graphModelInstance.getPropertyContainer());
        for (ModelDataProducer modelDataProducer : graphModelInstance.getModelInstanceData()) {
            if (modelDataProducer.isEnabled()) {
                modelDataProducer.fillData(modelInstanceData);
                shaderContext.setModelInstanceData(modelInstanceData);
                for (Uniform uniform : localUniforms.values()) {
                    uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
                }
                for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
                    uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
                }
                modelInstanceData.render(program, getAttributeLocations(modelInstanceData.getVertexAttributes()));
            }
        }
    }

    private int[] getAttributeLocations(final VertexAttributes attrs) {
        tempArray.clear();
        final int n = attrs.size();
        for (int i = 0; i < n; i++) {
            Attribute attribute = attributes.get(attrs.get(i).alias);
            if (attribute != null)
                tempArray.add(attribute.getLocation());
            else
                tempArray.add(-1);
        }
        return tempArray.items;
    }

    @Override
    public void end() {
        if (currentMesh != null) {
            currentMesh.unbind(program, tempArray.items);
            currentMesh = null;
        }
        super.end();
    }
}
