package com.gempukku.libgdx.graph.ui.shader.model;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.model.attribute.AttributeColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.model.provided.InstanceIdShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.model.provided.ModelFragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.model.producer.EndModelShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeNormalBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeTangentBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.ColorAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.FloatAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.TextureAttributeBoxProducer;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIModelShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    private static Map<String, GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer<ShaderFieldType> producer) {
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
        register(new GraphBoxProducerImpl<ShaderFieldType>(new AttributeColorShaderNodeConfiguration()));

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

        register(new GraphBoxProducerImpl<ShaderFieldType>(new ModelFragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new InstanceIdShaderNodeConfiguration()));
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
