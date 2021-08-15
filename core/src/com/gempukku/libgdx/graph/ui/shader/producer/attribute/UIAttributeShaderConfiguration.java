package com.gempukku.libgdx.graph.ui.shader.producer.attribute;

import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UIAttributeShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new FloatAttributeBoxProducer());
        register(new RandomFloatAttributeBoxProducer());
        register(new Vector2AttributeBoxProducer());
        register(new Vector3AttributeBoxProducer());
        register(new ColorAttributeBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return propertyProducers;
    }
}