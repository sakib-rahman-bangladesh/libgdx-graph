package com.gempukku.libgdx.graph.plugin.models.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeTangentShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributeTangentShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributeTangentShaderNodeBuilder() {
        super(new AttributeTangentShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Tangent(), "vec3");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
            vertexShaderBuilder.addMainLine("// Attribute Tangent Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((u_worldTrans * skinning * vec4(a_tangent, 0.0)).xyz);");

            return LibGDXCollections.singletonMap("tangent", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("object")) {
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((skinning * vec4(a_tangent, 0.0)).xyz);");
            return LibGDXCollections.singletonMap("tangent", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue
            data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder
                                                                                          vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext
                                                                                          graphShaderContext, GraphShader graphShader) {
        VertexAttribute attribute = VertexAttribute.Tangent();

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans);
            copyAttributeToFragmentShader(attribute, "v_tangent_world", "normalize((u_worldTrans * skinning * vec4(a_tangent, 0.0)).xyz)",
                    vertexShaderBuilder, fragmentShaderBuilder);

            return LibGDXCollections.singletonMap("tangent", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_tangent_world"));
        } else if (coordinates.equals("object")) {
            copyAttributeToFragmentShader(attribute, "v_tangent_object", "normalize((skinning * vec4(a_tangent, 0.0)).xyz)",
                    vertexShaderBuilder, fragmentShaderBuilder);

            return LibGDXCollections.singletonMap("tangent", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_tangent_object"));
        }
        throw new IllegalArgumentException();
    }
}
