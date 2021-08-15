package com.gempukku.libgdx.graph.field;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class FloatFieldType implements ShaderFieldType, PipelineFieldType {
    @Override
    public boolean accepts(Object value) {
        return value instanceof Number || value instanceof FloatProvider;
    }

    @Override
    public String getShaderType() {
        return "float";
    }

    @Override
    public Object convert(Object value) {
        if (value instanceof Number)
            return ((Number) value).floatValue();
        return ((FloatProvider) value).get();
    }

    @Override
    public String getName() {
        return "Float";
    }

    @Override
    public boolean isTexture() {
        return false;
    }

    @Override
    public Object convertFromJson(JsonValue data) {
        return data.getFloat("x");
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsUniform(CommonShaderBuilder commonShaderBuilder, JsonValue data, final PropertySource propertySource) {
        String variableName = "u_property_" + propertySource.getPropertyIndex();
        commonShaderBuilder.addUniformVariable(variableName, getShaderType(), false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(propertySource.getPropertyName());
                        value = propertySource.getValueToUse(value);
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                });
        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsVertexAttribute(VertexShaderBuilder vertexShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, attributeName), getShaderType());

        return new DefaultFieldOutput(getName(), attributeName);
    }

    @Override
    public GraphShaderNodeBuilder.FieldOutput addAsFragmentAttribute(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, JsonValue data, PropertySource propertySource) {
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, attributeName), getShaderType());
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, getShaderType());
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, getShaderType());
        }

        return new DefaultFieldOutput(getName(), variableName);
    }

    @Override
    public int setValueInAttributesArray(float[] vertices, int startIndex, Object value) {
        vertices[startIndex + 0] = ((Number) value).floatValue();
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return getName().equals(((FieldType) obj).getName());
    }
}
