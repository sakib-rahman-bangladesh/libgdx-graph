package com.gempukku.libgdx.graph.shader.model.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.model.attribute.AttributeColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributeColorShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributeColorShaderNodeBuilder() {
        super(new AttributeColorShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.ColorPacked(), ShaderProgram.COLOR_ATTRIBUTE, "vec4");

        return LibGDXCollections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Vector4, "a_color"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue
            data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder
                                                                                          vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext
                                                                                          graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.ColorPacked(), ShaderProgram.COLOR_ATTRIBUTE, "vec4");

        vertexShaderBuilder.addMainLine("// Attribute Normal Node");
        if (!vertexShaderBuilder.hasVaryingVariable("v_color")) {
            vertexShaderBuilder.addVaryingVariable("v_color", "vec4");
            vertexShaderBuilder.addMainLine("v_color = a_color;");

            fragmentShaderBuilder.addVaryingVariable("v_color", "vec4");
        }
        return LibGDXCollections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Vector4, "v_color"));
    }
}
