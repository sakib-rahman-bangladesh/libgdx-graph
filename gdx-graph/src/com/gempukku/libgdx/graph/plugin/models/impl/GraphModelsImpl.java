package com.gempukku.libgdx.graph.plugin.models.impl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.RuntimePipelinePlugin;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.property.PropertyContainerImpl;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.util.Comparator;

public class GraphModelsImpl implements GraphModels, RuntimePipelinePlugin {
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

    private Array<GraphModelImpl> preparedForRendering = new Array<>();

    private ObjectMap<String, ObjectSet<GraphModelImpl>> modelsByTag = new ObjectMap<>();
    private ObjectMap<String, ObjectMap<String, PropertySource>> propertiesByTag = new ObjectMap<>();
    private ObjectMap<String, PropertyContainerImpl> propertiesForTag = new ObjectMap<>();

    public void registerTag(String tag, ModelGraphShader shader) {
        modelsByTag.put(tag, new ObjectSet<GraphModelImpl>());
        propertiesByTag.put(tag, shader.getProperties());
        propertiesForTag.put(tag, new PropertyContainerImpl());
    }

    public void prepareForRendering(Camera camera, Iterable<String> modelTags) {
        cameraPosition.set(camera.position);
        order = null;
        preparedForRendering.clear();
        for (String modelTag : modelTags) {
            for (GraphModelImpl graphModel : modelsByTag.get(modelTag)) {
                preparedForRendering.add(graphModel);
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

    public Iterable<? extends GraphModelImpl> getModels() {
        return preparedForRendering;
    }

    public boolean hasModelWithTag(String tag) {
        return !modelsByTag.get(tag).isEmpty();
    }

    public PropertyContainer getGlobalProperties(String tag) {
        return propertiesForTag.get(tag);
    }

    @Override
    public GraphModel addModel(String tag, RenderableModel model) {
        GraphModelImpl graphModel = new GraphModelImpl(tag, model);
        modelsByTag.get(tag).add(graphModel);
        return graphModel;
    }

    @Override
    public void removeModel(GraphModel model) {
        GraphModelImpl graphModel = (GraphModelImpl) model;
        modelsByTag.get(graphModel.getTag()).remove(graphModel);
    }

    @Override
    public void setGlobalProperty(String tag, String name, Object value) {
        propertiesForTag.get(tag).setValue(name, value);
    }

    @Override
    public void unsetGlobalProperty(String tag, String name) {
        propertiesForTag.get(tag).remove(name);
    }

    @Override
    public Object getGlobalProperty(String tag, String name) {
        return propertiesForTag.get(tag).getValue(name);
    }

    @Override
    public void update(TimeProvider timeProvider) {

    }

    private static class DistanceRenderableSorter implements Comparator<GraphModelImpl> {
        private Vector3 cameraPosition;
        private Order order;

        public void sort(Vector3 cameraPosition, Array<GraphModelImpl> renderables, Order order) {
            this.cameraPosition = cameraPosition;
            this.order = order;
            renderables.sort(this);
        }

        @Override
        public int compare(GraphModelImpl o1, GraphModelImpl o2) {
            Vector3 position1 = o1.getRenderableModel().getPosition();
            Vector3 position2 = o2.getRenderableModel().getPosition();
            final float dst = (int) (1000f * cameraPosition.dst2(position1)) - (int) (1000f * cameraPosition.dst2(position2));
            return order.result(dst);
        }
    }
}
