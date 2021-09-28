package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.models.attribute.*;
import com.gempukku.libgdx.graph.plugin.models.material.ColorMaterialShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.material.FloatMaterialShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.material.TextureMaterialShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.producer.EndGraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.provided.InstanceIdShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.models.provided.ModelFragmentCoordinateShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
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
        addGraphShaderNodeBuilder(new FloatMaterialShaderNodeBuilder("Shininess", "Shininess", FloatAttribute.ShininessAlias));
        addGraphShaderNodeBuilder(new FloatMaterialShaderNodeBuilder("AlphaTest", "Alpha test", FloatAttribute.AlphaTestAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("AmbientTexture", "Ambient texture", TextureAttribute.AmbientAlias));
        addGraphShaderNodeBuilder(new ColorMaterialShaderNodeBuilder("AmbientColor", "Ambient color", ColorAttribute.AmbientAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("BumpTexture", "Bump texture", TextureAttribute.BumpAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("DiffuseTexture", "Diffuse texture", TextureAttribute.DiffuseAlias));
        addGraphShaderNodeBuilder(new ColorMaterialShaderNodeBuilder("DiffuseColor", "Diffuse color", ColorAttribute.DiffuseAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("EmissiveTexture", "Emissive texture", TextureAttribute.EmissiveAlias));
        addGraphShaderNodeBuilder(new ColorMaterialShaderNodeBuilder("EmissiveColor", "Emissive color", ColorAttribute.EmissiveAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("NormalTexture", "Normal texture", TextureAttribute.NormalAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("ReflectionTexture", "Reflection texture", TextureAttribute.ReflectionAlias));
        addGraphShaderNodeBuilder(new ColorMaterialShaderNodeBuilder("ReflectionColor", "Reflection color", ColorAttribute.ReflectionAlias));
        addGraphShaderNodeBuilder(new TextureMaterialShaderNodeBuilder("SpecularTexture", "Specular texture", TextureAttribute.SpecularAlias));
        addGraphShaderNodeBuilder(new ColorMaterialShaderNodeBuilder("SpecularColor", "Specular color", ColorAttribute.SpecularAlias));

        // Provided
        addGraphShaderNodeBuilder(new ModelFragmentCoordinateShaderNodeBuilder());
        addGraphShaderNodeBuilder(new InstanceIdShaderNodeBuilder());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    @Override
    public Array<GraphShaderPropertyProducer> getPropertyProducers() {
        return graphShaderPropertyProducers;
    }

    @Override
    public GraphShaderNodeBuilder getGraphShaderNodeBuilder(String type) {
        return graphShaderNodeBuilders.get(type);
    }
}
