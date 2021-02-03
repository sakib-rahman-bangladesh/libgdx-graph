package com.gempukku.libgdx.graph.ui.shader.screen;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.screen.producer.EndScreenShaderBoxProducer;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIScreenShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    private static Map<String, GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer<ShaderFieldType> producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndScreenShaderBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
