package com.gempukku.libgdx.graph.plugin.models.material;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.material.ColorMaterialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ColorMaterialShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public ColorMaterialShaderNodeBuilder(String type, String name, String alias) {
        super(new ColorMaterialShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String name = "u_" + alias;

        Color defaultColor = Color.valueOf(data.getString("default"));

        // Need to make sure ColorAttribute class is loaded
        ColorAttribute dummy = ColorAttribute.createAmbient(0, 0, 0, 0);

        long attributeType = ColorAttribute.getAttributeType(alias);
        commonShaderBuilder.addUniformVariable(name, "vec4", false, new ModelsUniformSetters.MaterialColor(attributeType, defaultColor));

        return LibGDXCollections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
    }
}
