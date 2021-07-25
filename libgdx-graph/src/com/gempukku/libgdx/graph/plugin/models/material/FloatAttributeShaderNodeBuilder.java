package com.gempukku.libgdx.graph.plugin.models.material;

import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.material.FloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class FloatAttributeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public FloatAttributeShaderNodeBuilder(String type, String name, String alias) {
        super(new FloatAttributeShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String name = "u_" + alias;

        float defaultValue = data.getFloat("default");

        // Need to make sure FloatAttribute class is loaded
        FloatAttribute dummy = FloatAttribute.createAlphaTest(0f);

        long attributeType = FloatAttribute.getAttributeType(alias);
        commonShaderBuilder.addUniformVariable(name, "float", false, new ModelsUniformSetters.MaterialFloat(attributeType, defaultValue));

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
