package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModelInstance;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.plugin.models.TagOptimizationHint;
import com.gempukku.libgdx.graph.shader.TransformUpdate;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.util.Comparator;

public class GraphModelsImpl implements GraphModels, RuntimePipelinePlugin, Disposable {
    private enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private static DistanceRenderableSorter sorter = new DistanceRenderableSorter();

    private Vector3 cameraPosition = new Vector3();
    private Order order;

    private ObjectSet<IGraphModel> graphShaderModels = new ObjectSet<>();
    private ObjectSet<IGraphModelInstance> models = new ObjectSet<>();
    private ObjectMap<String, ObjectSet<IGraphModelInstance>> instancesByTag = new ObjectMap<>();

    private ObjectSet<IGraphModelInstance> tempForUniqness = new ObjectSet<>();
    private Array<IGraphModelInstance> preparedForRendering = new Array<>();

    private Matrix4 transformTempMatrix = new Matrix4();

    private Array<VertexAttribute> registeredAttributes = new Array<>();
    private VertexAttributes vertexAttributes;

    private int maxNumberOfBonesPerMesh;
    private int maxNumberOfBoneWeights;

    public GraphModelsImpl(int maxNumberOfBonesPerMesh, int maxNumberOfBoneWeights) {
        this.maxNumberOfBonesPerMesh = maxNumberOfBonesPerMesh;
        this.maxNumberOfBoneWeights = maxNumberOfBoneWeights;
    }

    @Override
    public GraphModel registerModel(Model model) {
        if (vertexAttributes == null) {
            int numberOfAttributes = registeredAttributes.size;

            VertexAttribute[] vertexAttributeArr = new VertexAttribute[numberOfAttributes + maxNumberOfBoneWeights];
            for (int i = 0; i < numberOfAttributes; i++) {
                vertexAttributeArr[i] = registeredAttributes.get(i);
            }
            for (int i = 0; i < maxNumberOfBoneWeights; i++) {
                vertexAttributeArr[numberOfAttributes + i] = VertexAttribute.BoneWeight(i);
            }
            vertexAttributes = new VertexAttributes(vertexAttributeArr);
        }
        ModelBasedGraphModel graphShaderModel = new ModelBasedGraphModel(model, vertexAttributes);
        graphShaderModels.add(graphShaderModel);
        return graphShaderModel;
    }

    @Override
    public void removeModel(GraphModel model) {
        IGraphModel iModel = getModel(model);
        ObjectSet.ObjectSetIterator<IGraphModelInstance> iterator = models.iterator();
        for (IGraphModelInstance modelInstance : iterator) {
            if (modelInstance.getModel() == model)
                iterator.remove();
        }
        iModel.dispose();
    }

    @Override
    public void addModelDefaultTag(GraphModel model, String tag) {
        addModelDefaultTag(model, tag, TagOptimizationHint.Flash);
    }

    @Override
    public void addModelDefaultTag(GraphModel model, String tag, TagOptimizationHint tagOptimizationHint) {
        getModel(model).addDefaultTag(tag, tagOptimizationHint);
    }

    @Override
    public void removeModelDefaultTag(GraphModel model, String tag) {
        getModel(model).removeDefaultTag(tag);
    }

    @Override
    public GraphModelInstance createModelInstance(GraphModel model) {
        return createModelInstance(model, ModelInstanceOptimizationHints.unoptimized);
    }

    @Override
    public GraphModelInstance createModelInstance(GraphModel model, ModelInstanceOptimizationHints modelInstanceOptimizationHints) {
        IGraphModel iModel = getModel(model);
        IGraphModelInstance graphModelInstance = iModel.createInstance(modelInstanceOptimizationHints);
        models.add(graphModelInstance);
        for (ObjectMap.Entry<String, TagOptimizationHint> defaultTag : iModel.getDefaultTags()) {
            addTag(graphModelInstance, defaultTag.key, defaultTag.value);
        }

        return graphModelInstance;
    }

    @Override
    public void destroyModelInstance(GraphModelInstance modelInstance) {
        models.remove(getModelInstance(modelInstance));
    }

    @Override
    public void updateTransform(GraphModelInstance modelInstance, TransformUpdate transformUpdate) {
        Matrix4 transformMatrix = getModelInstance(modelInstance).getTransformMatrix();
        transformTempMatrix.set(transformMatrix);
        transformUpdate.updateTransform(transformTempMatrix);
        transformMatrix.set(transformTempMatrix);
    }

    public AnimationController createAnimationController(GraphModelInstance modelInstance) {
        return getModelInstance(modelInstance).createAnimationController();
    }

    @Override
    public void addTag(GraphModelInstance modelInstance, String tag) {
        addTag(modelInstance, tag, TagOptimizationHint.Flash);
    }

    @Override
    public void addTag(GraphModelInstance modelInstance, String tag, TagOptimizationHint tagOptimizationHint) {
        IGraphModelInstance iModelInstance = getModelInstance(modelInstance);
        iModelInstance.addTag(tag, tagOptimizationHint);
        instancesByTag.get(tag).add(iModelInstance);
    }

    @Override
    public void removeTag(GraphModelInstance modelInstance, String tag) {
        IGraphModelInstance iModelInstance = getModelInstance(modelInstance);
        instancesByTag.get(tag).remove(iModelInstance);
        iModelInstance.removeTag(tag);
    }

    @Override
    public void removeAllTags(GraphModelInstance modelInstance) {
        IGraphModelInstance iModelInstance = getModelInstance(modelInstance);
        for (String tag : iModelInstance.getAllTags()) {
            instancesByTag.get(tag).remove(iModelInstance);
        }
        iModelInstance.removeAllTags();
    }

    @Override
    public void setProperty(GraphModelInstance modelInstance, String name, Object value) {
        getModelInstance(modelInstance).getPropertyContainer().setValue(name, value);
    }

    @Override
    public void unsetProperty(GraphModelInstance modelInstance, String name) {
        getModelInstance(modelInstance).getPropertyContainer().remove(name);
    }

    @Override
    public boolean hasTag(GraphModelInstance modelInstance, String tag) {
        return getModelInstance(modelInstance).hasTag(tag);
    }

    @Override
    public Object getProperty(GraphModelInstance modelInstance, String name) {
        return getModelInstance(modelInstance).getPropertyContainer().getValue(name);
    }

    private IGraphModel getModel(GraphModel model) {
        IGraphModel iModel = (IGraphModel) model;
        if (!graphShaderModels.contains(iModel))
            throw new IllegalArgumentException("Unable to find the graph model");
        return iModel;
    }

    private IGraphModelInstance getModelInstance(GraphModelInstance modelInstance) {
        IGraphModelInstance iModelInstance = (IGraphModelInstance) modelInstance;
        if (!models.contains(iModelInstance))
            throw new IllegalArgumentException("Unable to find the graph model instance");
        return iModelInstance;
    }

    public void registerTag(String tag, ModelGraphShader shader) {
        instancesByTag.put(tag, new ObjectSet<IGraphModelInstance>());

        for (VertexAttribute vertexAttribute : shader.getVertexAttributes()) {
            if (vertexAttribute.usage == VertexAttributes.Usage.ColorPacked)
                vertexAttribute = VertexAttribute.ColorUnpacked();
            if (!registeredAttributes.contains(vertexAttribute, false))
                registeredAttributes.add(vertexAttribute);
        }
    }

    public void prepareForRendering(Camera camera, Iterable<String> modelTags) {
        cameraPosition.set(camera.position);
        order = null;
        preparedForRendering.clear();
        tempForUniqness.clear();
        for (String modelTag : modelTags) {
            for (IGraphModelInstance iGraphModelInstance : instancesByTag.get(modelTag)) {
                if (tempForUniqness.add(iGraphModelInstance))
                    preparedForRendering.add(iGraphModelInstance);
            }
        }
    }

    public void orderFrontToBack() {
        if (order == Order.Back_To_Front)
            preparedForRendering.reverse();
        if (order == null)
            sorter.sort(cameraPosition, preparedForRendering, Order.Front_To_Back);
        order = Order.Front_To_Back;
    }

    public void orderBackToFront() {
        if (order == Order.Front_To_Back)
            preparedForRendering.reverse();
        if (order == null)
            sorter.sort(cameraPosition, preparedForRendering, Order.Back_To_Front);
        order = Order.Back_To_Front;
    }

    public Iterable<? extends IGraphModelInstance> getModels() {
        return preparedForRendering;
    }

    public boolean hasModelWithTag(String tag) {
        for (IGraphModelInstance value : models) {
            if (value.hasTag(tag))
                return true;
        }
        return false;
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }

    @Override
    public void dispose() {
        for (IGraphModel model : graphShaderModels) {
            model.dispose();
        }
        graphShaderModels.clear();
        models.clear();
    }

    private static class DistanceRenderableSorter implements Comparator<IGraphModelInstance> {
        private Vector3 cameraPosition;
        private Order order;
        private final Vector3 tmpV1 = new Vector3();
        private final Vector3 tmpV2 = new Vector3();

        public void sort(Vector3 cameraPosition, Array<IGraphModelInstance> renderables, Order order) {
            this.cameraPosition = cameraPosition;
            this.order = order;
            renderables.sort(this);
        }

        private Vector3 getTranslation(Matrix4 worldTransform, Vector3 output) {
            worldTransform.getTranslation(output);
            return output;
        }

        @Override
        public int compare(IGraphModelInstance o1, IGraphModelInstance o2) {
            getTranslation(o1.getTransformMatrix(), tmpV1);
            getTranslation(o2.getTransformMatrix(), tmpV2);
            final float dst = (int) (1000f * cameraPosition.dst2(tmpV1)) - (int) (1000f * cameraPosition.dst2(tmpV2));
            return order.result(dst);
        }
    }
}
