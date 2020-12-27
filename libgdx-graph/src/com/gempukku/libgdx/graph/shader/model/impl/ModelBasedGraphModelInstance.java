package com.gempukku.libgdx.graph.shader.model.impl;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelDataProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelInstanceDataImpl;
import com.gempukku.libgdx.graph.shader.model.GraphModel;
import com.gempukku.libgdx.graph.shader.model.ModelInstanceOptimizationHints;
import com.gempukku.libgdx.graph.shader.model.TagOptimizationHint;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;

public class ModelBasedGraphModelInstance implements IGraphModelInstance {
    private IGraphModel model;
    private ModelInstance modelInstance;
    private ModelInstanceOptimizationHints modelInstanceOptimizationHints;
    private ObjectMap<String, TagOptimizationHint> tags = new ObjectMap<>();
    private PropertyContainerImpl propertyContainer = new PropertyContainerImpl();
    private Array<ModelDataProducer> dataProducers = new Array<>();

    public ModelBasedGraphModelInstance(IGraphModel model, ModelInstance modelInstance, ModelInstanceOptimizationHints modelInstanceOptimizationHints) {
        this.model = model;
        this.modelInstance = modelInstance;
        this.modelInstanceOptimizationHints = modelInstanceOptimizationHints;
        initializeDataProducers();
    }

    private void initializeDataProducers() {
        for (Node node : modelInstance.nodes) {
            initializeDataProducers(node);
        }
    }

    private void initializeDataProducers(Node node) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {
                if (nodePart.enabled)
                    dataProducers.add(createDataProducer(node, nodePart));
            }
        }

        for (Node child : node.getChildren()) {
            initializeDataProducers(child);
        }
    }

    private ModelDataProducer createDataProducer(Node node, NodePart nodePart) {
        return new NodePartModelDataProducer(node, nodePart);
    }

    @Override
    public void addTag(String tag, TagOptimizationHint tagOptimizationHint) {
        tags.put(tag, tagOptimizationHint);
    }

    @Override
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public void removeAllTags() {
        tags.clear();
    }

    @Override
    public boolean hasTag(String tag) {
        return tags.containsKey(tag);
    }

    @Override
    public PropertyContainerImpl getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public Matrix4 getTransformMatrix() {
        return modelInstance.transform;
    }

    @Override
    public AnimationController createAnimationController() {
        return new AnimationController(modelInstance);
    }

    @Override
    public Iterable<ModelDataProducer> getModelInstanceData() {
        return dataProducers;
    }

    @Override
    public GraphModel getModel() {
        return model;
    }

    private class NodePartModelDataProducer implements ModelDataProducer, ModelInstanceDataImpl.MeshRenderer {
        private Node node;
        private NodePart nodePart;

        public NodePartModelDataProducer(Node node, NodePart nodePart) {
            this.node = node;
            this.nodePart = nodePart;
        }

        @Override
        public void fillData(ModelInstanceDataImpl data) {
            data.setMaterial(nodePart.material);
            data.setBones(nodePart.bones);
            data.setMeshRenderer(this);
            data.setVertexAttributes(nodePart.meshPart.mesh.getVertexAttributes());

            Matrix4 out = data.getWorldTransform();
            if (nodePart.bones == null && modelInstance.transform != null)
                out.set(modelInstance.transform).mul(node.globalTransform);
            else if (modelInstance.transform != null)
                out.set(modelInstance.transform);
            else
                out.idt();
        }

        @Override
        public void render(ShaderProgram shaderProgram, int[] attributeLocations) {
            nodePart.meshPart.mesh.bind(shaderProgram, attributeLocations);
            nodePart.meshPart.render(shaderProgram);
        }
    }
}
