package com.gempukku.libgdx.graph.plugin.models.design;

import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.config.provided.InstanceIdShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.config.provided.ModelFragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.design.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeNormalBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeTangentBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.ColorMaterialBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.FloatMaterialBoxProducer;
import com.gempukku.libgdx.graph.plugin.models.design.producer.material.TextureMaterialBoxProducer;
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

        register(new FloatMaterialBoxProducer("Shininess", "Shininess"));
        register(new FloatMaterialBoxProducer("AlphaTest", "Alpha test"));
        register(new TextureMaterialBoxProducer("AmbientTexture", "Ambient texture"));
        register(new ColorMaterialBoxProducer("AmbientColor", "Ambient color"));
        register(new TextureMaterialBoxProducer("BumpTexture", "Bump texture"));
        register(new TextureMaterialBoxProducer("DiffuseTexture", "Diffuse texture"));
        register(new ColorMaterialBoxProducer("DiffuseColor", "Diffuse color"));
        register(new TextureMaterialBoxProducer("EmissiveTexture", "Emissive texture"));
        register(new ColorMaterialBoxProducer("EmissiveColor", "Emissive color"));
        register(new TextureMaterialBoxProducer("NormalTexture", "Normal texture"));
        register(new TextureMaterialBoxProducer("ReflectionTexture", "Reflection texture"));
        register(new ColorMaterialBoxProducer("ReflectionColor", "Reflection color"));
        register(new TextureMaterialBoxProducer("SpecularTexture", "Specular texture"));
        register(new ColorMaterialBoxProducer("SpecularColor", "Specular color"));

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
