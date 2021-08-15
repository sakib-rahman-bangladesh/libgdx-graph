package com.gempukku.libgdx.graph.shader.common.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.attribute.Vector3AttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class Vector3AttributeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public Vector3AttributeShaderNodeBuilder() {
        super(new Vector3AttributeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String attributeName = graphShader.addAdditionalAttribute(data.getString("name"), "a_" + nodeId, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Vector3),
                new Vector3(data.getFloat("x"), data.getFloat("y"), data.getFloat("z")));
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 3, attributeName), "vec3");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, attributeName));
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String varyingName = "v_" + nodeId;
        String attributeName = graphShader.addAdditionalAttribute(data.getString("name"), "a_" + nodeId, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Vector3),
                new Vector3(data.getFloat("x"), data.getFloat("y"), data.getFloat("z")));
        VertexAttribute attribute = new VertexAttribute(1024, 3, attributeName);
        copyAttributeToFragmentShader(attribute, varyingName, vertexShaderBuilder, fragmentShaderBuilder);
        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, varyingName));
    }
}
