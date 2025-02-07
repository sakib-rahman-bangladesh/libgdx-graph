package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;

public abstract class ConfigurationShaderNodeBuilder implements GraphShaderNodeBuilder {
    private NodeConfiguration configuration;

    public ConfigurationShaderNodeBuilder(NodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        ObjectMap<String, FieldOutput> inputMap = new ObjectMap<>();
        for (ObjectMap.Entry<String, Array<FieldOutput>> entry : inputs.entries()) {
            if (entry.value != null && entry.value.size == 1)
                inputMap.put(entry.key, entry.value.get(0));
        }

        return buildVertexNodeSingleInputs(designTime, nodeId, data, inputMap, producedOutputs, vertexShaderBuilder, graphShaderContext, graphShader);
    }

    protected abstract ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        ObjectMap<String, FieldOutput> inputMap = new ObjectMap<>();
        for (ObjectMap.Entry<String, Array<FieldOutput>> entry : inputs.entries()) {
            if (entry.value != null && entry.value.size == 1)
                inputMap.put(entry.key, entry.value.get(0));
        }
        return buildFragmentNodeSingleInputs(designTime, nodeId, data, inputMap, producedOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphShaderContext, graphShader);
    }

    protected abstract ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader);

    protected void loadFragmentIfNotDefined(CommonShaderBuilder shaderBuilder, String fragmentName) {
        if (!shaderBuilder.containsFunction(fragmentName))
            shaderBuilder.addFunction(fragmentName, GLSLFragmentReader.getFragment(fragmentName));
    }

    protected void copyAttributeToFragmentShader(VertexAttribute attribute, String varyingName,
                                                 VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        copyAttributeToFragmentShader(attribute, varyingName, attribute.alias, vertexShaderBuilder, fragmentShaderBuilder);
    }

    protected void copyAttributeToFragmentShader(VertexAttribute attribute, String varyingName, String attributeValue,
                                                 VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        String type;
        if (attribute.numComponents == 1)
            type = "float";
        else if (attribute.numComponents == 2)
            type = "vec2";
        else if (attribute.numComponents == 3)
            type = "vec3";
        else if (attribute.numComponents == 4)
            type = "vec4";
        else
            throw new IllegalArgumentException("Unable to figure out variable type from attribute with numComponents: " + attribute.numComponents);

        vertexShaderBuilder.addAttributeVariable(attribute, type);
        if (!vertexShaderBuilder.hasVaryingVariable(varyingName)) {
            vertexShaderBuilder.addVaryingVariable(varyingName, type);
            vertexShaderBuilder.addMainLine(varyingName + " = " + attributeValue + ";");

            fragmentShaderBuilder.addVaryingVariable(varyingName, type);
        }
    }
}
