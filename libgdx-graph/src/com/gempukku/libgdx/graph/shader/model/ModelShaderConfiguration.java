package com.gempukku.libgdx.graph.shader.model;

import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.model.attribute.AttributeColorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.attribute.AttributeNormalShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.attribute.AttributePositionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.attribute.AttributeTangentShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.attribute.AttributeUVShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.material.ColorAttributeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.material.FloatAttributeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.material.TextureAttributeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.provided.InstanceIdShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.model.provided.ModelFragmentCoordinateShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public class ModelShaderConfiguration implements GraphConfiguration {
    public static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    public static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    static {
        // End
        addGraphShaderNodeBuilder(new EndGraphShaderNodeBuilder());

        // Attributes
        addGraphShaderNodeBuilder(new AttributePositionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeNormalShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeTangentShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeUVShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeColorShaderNodeBuilder());

        // Material
        addGraphShaderNodeBuilder(new FloatAttributeShaderNodeBuilder("Shininess", "Shininess", FloatAttribute.ShininessAlias));
        addGraphShaderNodeBuilder(new FloatAttributeShaderNodeBuilder("AlphaTest", "Alpha test", FloatAttribute.AlphaTestAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("AmbientTexture", "Ambient texture", TextureAttribute.AmbientAlias));
        addGraphShaderNodeBuilder(new ColorAttributeShaderNodeBuilder("AmbientColor", "Ambient color", ColorAttribute.AmbientAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("BumpTexture", "Bump texture", TextureAttribute.BumpAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("DiffuseTexture", "Diffuse texture", TextureAttribute.DiffuseAlias));
        addGraphShaderNodeBuilder(new ColorAttributeShaderNodeBuilder("DiffuseColor", "Diffuse color", ColorAttribute.DiffuseAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("EmissiveTexture", "Emissive texture", TextureAttribute.EmissiveAlias));
        addGraphShaderNodeBuilder(new ColorAttributeShaderNodeBuilder("EmissiveColor", "Emissive color", ColorAttribute.EmissiveAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("NormalTexture", "Normal texture", TextureAttribute.NormalAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("ReflectionTexture", "Reflection texture", TextureAttribute.ReflectionAlias));
        addGraphShaderNodeBuilder(new ColorAttributeShaderNodeBuilder("ReflectionColor", "Reflection color", ColorAttribute.ReflectionAlias));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("SpecularTexture", "Specular texture", TextureAttribute.SpecularAlias));
        addGraphShaderNodeBuilder(new ColorAttributeShaderNodeBuilder("SpecularColor", "Specular color", ColorAttribute.SpecularAlias));

        // Provided
        addGraphShaderNodeBuilder(new ModelFragmentCoordinateShaderNodeBuilder());
        addGraphShaderNodeBuilder(new InstanceIdShaderNodeBuilder());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    public ModelShaderConfiguration() {

    }

    @Override
    public Array<GraphShaderPropertyProducer> getPropertyProducers() {
        return graphShaderPropertyProducers;
    }

    @Override
    public ObjectMap<String, GraphShaderNodeBuilder> getGraphShaderNodeBuilders() {
        return graphShaderNodeBuilders;
    }
}
