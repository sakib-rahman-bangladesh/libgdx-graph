package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

import java.util.HashMap;
import java.util.Map;

public abstract class GraphShader extends UniformCachingShader implements GraphShaderContext {
    private Array<Disposable> disposableList = new Array<>();
    protected ObjectMap<String, PropertySource> propertySourceMap = new ObjectMap<>();
    private Map<String, AttributeDefinition> additionalAttributes = new HashMap<>();
    private ShaderProgram shaderProgram;
    private VertexAttributes vertexAttributes;
    private int[] attributeLocations;
    private String tag;

    public GraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public void setVertexAttributes(VertexAttributes vertexAttributes) {
        this.vertexAttributes = vertexAttributes;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public String addAdditionalAttribute(String name, String suggestedAlias, ShaderFieldType fieldType, Object value) {
        if (additionalAttributes.containsKey(name)) {
            AttributeDefinition attributeDefinition = additionalAttributes.get(name);
            if (attributeDefinition.getFieldType() != fieldType)
                throw new IllegalArgumentException("Shader already has attribute with this name, and type does not match");
            return attributeDefinition.getAlias();
        } else {
            additionalAttributes.put(name, new AttributeDefinition(suggestedAlias, fieldType, value));
            return suggestedAlias;
        }
    }

    public Map<String, AttributeDefinition> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public int[] getAttributeLocations() {
        if (attributeLocations == null) {
            IntArray tempArray = new IntArray();
            final int n = vertexAttributes.size();
            for (int i = 0; i < n; i++) {
                Attribute attribute = attributes.get(vertexAttributes.get(i).alias);
                if (attribute != null)
                    tempArray.add(attribute.getLocation());
                else
                    tempArray.add(-1);
            }
            attributeLocations = tempArray.items;
        }
        return attributeLocations;
    }

    public void init() {
        init(shaderProgram);
    }

    public void addPropertySource(String name, PropertySource propertySource) {
        propertySourceMap.put(name, propertySource);
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    @Override
    public void addManagedResource(Disposable disposable) {
        disposableList.add(disposable);
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
        disposableList.clear();

        if (shaderProgram != null)
            shaderProgram.dispose();
        super.dispose();
    }
}
