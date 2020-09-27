package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

public class GraphShaderModelInstance {
    private String id;
    private GraphShaderModel model;
    private ModelInstance modelInstance;
    private ObjectSet<String> tags = new ObjectSet<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public GraphShaderModelInstance(String id, GraphShaderModel model, ModelInstance modelInstance) {
        this.id = id;
        this.model = model;
        this.modelInstance = modelInstance;
    }

    public String getId() {
        return id;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public void removeAllTags() {
        tags.clear();
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public void unsetProperty(String name) {
        properties.remove(name);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public Matrix4 getTransformMatrix() {
        return modelInstance.transform;
    }

    public GraphShaderModel getModel() {
        return model;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        modelInstance.getRenderables(renderables, pool);
    }
}
