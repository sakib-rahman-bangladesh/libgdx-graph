package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.config.provided.InstanceIdShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.config.provided.ModelFragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeNormalBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeTangentBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.ColorAttributeBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.FloatAttributeBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.TextureAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIModelShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndModelShaderBoxProducer());

        register(new AttributePositionBoxProducer());
        register(new AttributeNormalBoxProducer());
        register(new AttributeTangentBoxProducer());
        register(new AttributeUVBoxProducer());
        register(new GraphBoxProducerImpl(new AttributeColorShaderNodeConfiguration()));

        register(new FloatAttributeBoxProducer("Shininess", "Shininess"));
        register(new FloatAttributeBoxProducer("AlphaTest", "Alpha test"));
        register(new TextureAttributeBoxProducer("AmbientTexture", "Ambient texture"));
        register(new ColorAttributeBoxProducer("AmbientColor", "Ambient color"));
        register(new TextureAttributeBoxProducer("BumpTexture", "Bump texture"));
        register(new TextureAttributeBoxProducer("DiffuseTexture", "Diffuse texture"));
        register(new ColorAttributeBoxProducer("DiffuseColor", "Diffuse color"));
        register(new TextureAttributeBoxProducer("EmissiveTexture", "Emissive texture"));
        register(new ColorAttributeBoxProducer("EmissiveColor", "Emissive color"));
        register(new TextureAttributeBoxProducer("NormalTexture", "Normal texture"));
        register(new TextureAttributeBoxProducer("ReflectionTexture", "Reflection texture"));
        register(new ColorAttributeBoxProducer("ReflectionColor", "Reflection color"));
        register(new TextureAttributeBoxProducer("SpecularTexture", "Specular texture"));
        register(new ColorAttributeBoxProducer("SpecularColor", "Specular color"));

        register(new GraphBoxProducerImpl(new ModelFragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new InstanceIdShaderNodeConfiguration()));
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
