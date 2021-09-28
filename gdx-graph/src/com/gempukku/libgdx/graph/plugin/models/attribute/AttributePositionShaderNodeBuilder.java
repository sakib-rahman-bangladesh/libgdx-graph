package com.gempukku.libgdx.graph.plugin.models.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
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
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), "vec3");

        vertexShaderBuilder.addMainLine("// Attribute Position Node");
        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
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
        String coordinates = data.getString("coordinates");
        if (coordinates.equals("world")) {
            fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");

            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_world"));
        } else if (coordinates.equals("object")) {
            VertexAttribute attribute = VertexAttribute.Position();
            copyAttributeToFragmentShader(attribute, "v_position_object", "(skinning * vec4(a_position, 1.0)).xyz",
                    vertexShaderBuilder, fragmentShaderBuilder);

            return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_object"));
        }
        throw new IllegalArgumentException();
    }
}
