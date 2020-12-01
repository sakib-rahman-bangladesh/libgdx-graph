package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;

public class ModelPropertyShaderNodeBuilder implements GraphShaderNodeBuilder {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration<ShaderFieldType> getConfiguration(JsonValue data) {
        final String name = data.getString("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf(data.getString("type"));

        return new PropertyNodeConfiguration<ShaderFieldType>(name, propertyType);
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
        final ShaderFieldType propertyType = ShaderFieldType.valueOf(data.getString("type"));

        switch (propertyType) {
            case Vector4:
                return buildColorPropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Float:
                return buildFloatPropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Vector2:
                return buildVector2PropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Vector3:
                return buildVector3PropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case TextureRegion:
                return buildTexturePropertyNode(nodeId, name, data, graphShaderContext, commonShaderBuilder);
        }

        return null;
    }


    private ObjectMap<String, DefaultFieldOutput> buildColorPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                         CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof Color))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Color) value);
                    }
                }, "Property - " + name);

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector4, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildFloatPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                         CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "float", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof Number))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                }, "Property - " + name);

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector2PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                           CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec2", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof Vector2))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector2) value);
                    }
                }, "Property - " + name);

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector3PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                           CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec3", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof Vector3))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector3) value);
                    }
                }, "Property - " + name);

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildTexturePropertyNode(String nodeId, final String name, JsonValue data, final GraphShaderContext graphShaderContext,
                                                                           CommonShaderBuilder commonShaderBuilder) {
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + nodeId;
        String uvTransformVariableName = "u_uvTransform_" + nodeId;
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof TextureRegion))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        textureDescriptor.texture = ((TextureRegion) value).getTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Texture property - " + name);
        commonShaderBuilder.addUniformVariable(uvTransformVariableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Object value = shaderContext.getProperty(name);
                        if (!(value instanceof TextureRegion))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformVariableName, textureVariableName));
    }
}
