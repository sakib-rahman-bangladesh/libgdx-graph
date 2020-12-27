package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

public abstract class GraphShader extends UniformCachingShader implements GraphShaderContext {
    private Array<Disposable> disposableList = new Array<>();
    protected ObjectMap<String, PropertySource> propertySourceMap;
    private ShaderProgram shaderProgram;
    private TimeProvider timeProvider;
    private GraphShaderEnvironment environment;
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

    public void setPropertySourceMap(ObjectMap<String, PropertySource> propertySourceMap) {
        this.propertySourceMap = propertySourceMap;
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void setEnvironment(GraphShaderEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    @Override
    public GraphShaderEnvironment getEnvironment() {
        return environment;
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
