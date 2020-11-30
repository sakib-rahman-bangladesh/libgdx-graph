package com.gempukku.libgdx.graph.ui.shader.model;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.provided.InstanceIdShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.provided.ModelFragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.model.producer.EndShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeNormalBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeTangentBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.ColorAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.FloatAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.material.TextureAttributeBoxProducer;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIModelShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new LinkedHashSet<>();

    static {
        graphBoxProducers.add(new EndShaderBoxProducer());

        graphBoxProducers.add(new AttributePositionBoxProducer());
        graphBoxProducers.add(new AttributeNormalBoxProducer());
        graphBoxProducers.add(new AttributeTangentBoxProducer());
        graphBoxProducers.add(new AttributeUVBoxProducer());

        graphBoxProducers.add(new FloatAttributeBoxProducer("Shininess", "Shininess"));
        graphBoxProducers.add(new FloatAttributeBoxProducer("AlphaTest", "Alpha test"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("AmbientTexture", "Ambient texture"));
        graphBoxProducers.add(new ColorAttributeBoxProducer("AmbientColor", "Ambient color"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("BumpTexture", "Bump texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("DiffuseTexture", "Diffuse texture"));
        graphBoxProducers.add(new ColorAttributeBoxProducer("DiffuseColor", "Diffuse color"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("EmissiveTexture", "Emissive texture"));
        graphBoxProducers.add(new ColorAttributeBoxProducer("EmissiveColor", "Emissive color"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("NormalTexture", "Normal texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("ReflectionTexture", "Reflection texture"));
        graphBoxProducers.add(new ColorAttributeBoxProducer("ReflectionColor", "Reflection color"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("SpecularTexture", "Specular texture"));
        graphBoxProducers.add(new ColorAttributeBoxProducer("SpecularColor", "Specular color"));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ModelFragmentCoordinateShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new InstanceIdShaderNodeConfiguration()));
    }

    @Override
    public Set<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers;
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
