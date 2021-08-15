package com.gempukku.libgdx.graph.shader.common.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.field.RandomFloatProvider;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.attribute.RandomFloatAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class RandomFloatAttributeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public RandomFloatAttributeShaderNodeBuilder() {
        super(new RandomFloatAttributeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        RandomFloatProvider floatProvider = new RandomFloatProvider(data.getFloat("min"), data.getFloat("max"));
        String attributeName = graphShader.addAdditionalAttribute(data.getString("name"), "a_" + nodeId, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Float),
                floatProvider);
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, attributeName), "float");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, attributeName));
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String varyingName = "v_" + nodeId;
        RandomFloatProvider floatProvider = new RandomFloatProvider(data.getFloat("min"), data.getFloat("max"));
        String attributeName = graphShader.addAdditionalAttribute(data.getString("name"), "a_" + nodeId, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Float),
                floatProvider);
        VertexAttribute attribute = new VertexAttribute(1024, 1, attributeName);
        copyAttributeToFragmentShader(attribute, varyingName, vertexShaderBuilder, fragmentShaderBuilder);
        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, varyingName));
    }
}
