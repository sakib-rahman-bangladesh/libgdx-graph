package com.gempukku.libgdx.graph.plugin.models.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributePositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributePositionShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributePositionShaderNodeBuilder() {
        super(new AttributePositionShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
            vertexShaderBuilder.addMainLine("// Attribute Position Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = (u_worldTrans * skinning * vec4(a_position, 1.0)).xyz;");

            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("object")) {
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = (skinning * vec4(a_position, 1.0)).xyz;");
            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");

        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_position_world")) {
                vertexShaderBuilder.addMainLine("// Attribute Position Node");
                vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans);
                vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
                vertexShaderBuilder.addMainLine("v_position_world = (u_worldTrans * skinning * vec4(a_position, 1.0)).xyz;");

                fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");
            }

            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_world"));
        } else if (coordinates.equals("object")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_position_object")) {
                vertexShaderBuilder.addMainLine("// Attribute Position Node");
                vertexShaderBuilder.addVaryingVariable("v_position_object", "vec3");
                vertexShaderBuilder.addMainLine("v_position_object = (skinning * vec4(a_position, 1.0)).xyz;");

                fragmentShaderBuilder.addVaryingVariable("v_position_object", "vec3");
            }

            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_object"));
        }
        throw new IllegalArgumentException();
    }
}
