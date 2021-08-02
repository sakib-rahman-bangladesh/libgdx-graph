package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class PropertyAsUniformShaderNodeBuilder implements GraphShaderNodeBuilder {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        return new PropertyNodeConfiguration(name, propertyType);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, vertexShaderBuilder, graphShaderContext, graphShader);
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                                      VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, fragmentShaderBuilder, graphShaderContext, graphShader);
    }

    private ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                                     CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final String propertyType = data.getString("type");

        ShaderFieldType fieldType = ShaderFieldTypeRegistry.findShaderFieldType(propertyType);
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        return LibGDXCollections.singletonMap("value", fieldType.addAsUniform(commonShaderBuilder, data, propertySource));
    }
}
