package com.gempukku.libgdx.graph.plugin.models.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeNormalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributeNormalShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributeNormalShaderNodeBuilder() {
        super(new AttributeNormalShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Normal(), "vec3");

        vertexShaderBuilder.addMainLine("// Attribute Normal Node");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("object")) {
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((skinning * vec4(a_normal, 0.0)).xyz);");
            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("world")) {
            vertexShaderBuilder.addUniformVariable("u_normalWorldTrans", "mat4", false, ModelsUniformSetters.normalWorldTrans);
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
        VertexAttribute attribute = VertexAttribute.Normal();
        String coordinates = data.getString("coordinates");
        if (coordinates.equals("object")) {
            copyAttributeToFragmentShader(attribute, "v_normal_object", "normalize((skinning * vec4(a_normal, 0.0)).xyz)",
                    vertexShaderBuilder, fragmentShaderBuilder);

            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_object"));
        } else if (coordinates.equals("world")) {
            vertexShaderBuilder.addUniformVariable("u_normalWorldTrans", "mat4", false, ModelsUniformSetters.normalWorldTrans);
            copyAttributeToFragmentShader(attribute, "v_normal_world", "normalize((u_normalWorldTrans * skinning * vec4(a_normal, 0.0)).xyz)",
                    vertexShaderBuilder, fragmentShaderBuilder);

            return LibGDXCollections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_world"));
        }
        throw new IllegalArgumentException();
    }
}
