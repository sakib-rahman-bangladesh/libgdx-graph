package com.gempukku.libgdx.graph.shader.model.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.model.attribute.AttributeNormalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributeNormalShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributeNormalShaderNodeBuilder() {
        super(new AttributeNormalShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Normal(), ShaderProgram.NORMAL_ATTRIBUTE, "vec3");

        vertexShaderBuilder.addMainLine("// Attribute Normal Node");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("object")) {
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((skinning * vec4(a_normal, 0.0)).xyz);");
            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("world")) {
            vertexShaderBuilder.addUniformVariable("u_normalWorldTrans", "mat4", false, UniformSetters.normalWorldTrans);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((u_normalWorldTrans * skinning * vec4(a_normal, 0.0)).xyz);");
            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue
            data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder
                                                                                          vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext
                                                                                          graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Normal(), ShaderProgram.NORMAL_ATTRIBUTE, "vec3");

        vertexShaderBuilder.addMainLine("// Attribute Normal Node");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("object")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_normal_object")) {
                vertexShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_object = normalize((skinning * vec4(a_normal, 0.0)).xyz);");

                fragmentShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
            }

            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_object"));
        } else if (coordinates.equals("world")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_normal_world")) {
                vertexShaderBuilder.addUniformVariable("u_normalWorldTrans", "mat4", false, UniformSetters.normalWorldTrans);
                vertexShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_world = normalize((u_normalWorldTrans * skinning * vec4(a_normal, 0.0)).xyz);");

                fragmentShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
            }

            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_world"));
        }
        throw new IllegalArgumentException();
    }
}
