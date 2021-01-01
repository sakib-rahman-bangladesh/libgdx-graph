package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.config.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class PropertyAsAttributeShaderNodeBuilder implements GraphShaderNodeBuilder {
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
        final String name = data.getString("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf(data.getString("type"));

        switch (propertyType) {
            case Vector4:
                return buildColorPropertyVertexNode(name, graphShaderContext, vertexShaderBuilder);
            case Float:
                return buildFloatPropertyVertexNode(name, graphShaderContext, vertexShaderBuilder);
            case Vector2:
                return buildVector2PropertyVertexNode(name, graphShaderContext, vertexShaderBuilder);
            case Vector3:
                return buildVector3PropertyVertexNode(name, graphShaderContext, vertexShaderBuilder);
            case TextureRegion:
                return buildTexturePropertyVertexNode(name, data, graphShaderContext, vertexShaderBuilder);
        }

        return null;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, Array<FieldOutput>> inputs, ObjectSet<String> producedOutputs,
                                                                      VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = data.getString("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf(data.getString("type"));

        switch (propertyType) {
            case Vector4:
                return buildColorPropertyFragmentNode(name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Float:
                return buildFloatPropertyFragmentNode(name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Vector2:
                return buildVector2PropertyFragmentNode(name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Vector3:
                return buildVector3PropertyFragmentNode(name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case TextureRegion:
                return buildTexturePropertyFragmentNode(name, data, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
        }

        return null;
    }

    private ObjectMap<String, DefaultFieldOutput> buildColorPropertyVertexNode(final String name, final GraphShaderContext graphShaderContext,
                                                                               VertexShaderBuilder vertexShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, attributeName), attributeName, "vec4");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector4, attributeName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildColorPropertyFragmentNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                 VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, attributeName), attributeName, "vec4");
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, "vec4");
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, "vec4");
        }

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector4, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildFloatPropertyVertexNode(final String name, final GraphShaderContext graphShaderContext,
                                                                               VertexShaderBuilder vertexShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, attributeName), attributeName, "float");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, attributeName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildFloatPropertyFragmentNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                 VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, attributeName), attributeName, "float");
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, "float");
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, "float");
        }

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector2PropertyVertexNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                 VertexShaderBuilder vertexShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 2, attributeName), attributeName, "vec2");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, attributeName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector2PropertyFragmentNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                   VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 2, attributeName), attributeName, "vec2");
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, "vec2");
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, "vec2");
        }

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector3PropertyVertexNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                 VertexShaderBuilder vertexShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 3, attributeName), attributeName, "vec3");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, attributeName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildVector3PropertyFragmentNode(final String name, final GraphShaderContext graphShaderContext,
                                                                                   VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);
        String attributeName = "a_property_" + propertySource.getPropertyIndex();
        String variableName = "v_property_" + propertySource.getPropertyIndex();

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 3, attributeName), attributeName, "vec3");
        if (!vertexShaderBuilder.hasVaryingVariable(variableName)) {
            vertexShaderBuilder.addVaryingVariable(variableName, "vec3");
            vertexShaderBuilder.addMainLine(variableName + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(variableName, "vec3");
        }

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildTexturePropertyVertexNode(final String name, JsonValue data, final GraphShaderContext graphShaderContext,
                                                                                 VertexShaderBuilder vertexShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformAttributeName = "a_property_" + propertySource.getPropertyIndex();
        vertexShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
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
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), uvTransformAttributeName, "vec4");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformAttributeName, textureVariableName));
    }

    private ObjectMap<String, DefaultFieldOutput> buildTexturePropertyFragmentNode(final String name, JsonValue data, final GraphShaderContext graphShaderContext,
                                                                                   VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        PropertySource propertySource = graphShaderContext.getPropertySource(name);

        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        if (data.has("minFilter"))
            textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
        if (data.has("magFilter"))
            textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
        if (data.has("uWrap"))
            textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data.has("vWrap"))
            textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        String textureVariableName = "u_property_" + propertySource.getPropertyIndex();
        String uvTransformAttributeName = "a_property_" + propertySource.getPropertyIndex();
        String uvTransformVariableName = "v_property_" + propertySource.getPropertyIndex();
        fragmentShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
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
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 4, uvTransformAttributeName), uvTransformAttributeName, "vec4");
        if (!vertexShaderBuilder.hasVaryingVariable(uvTransformVariableName)) {
            vertexShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
            vertexShaderBuilder.addMainLine(uvTransformVariableName + " = " + uvTransformAttributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(uvTransformVariableName, "vec4");
        }

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformVariableName, textureVariableName));
    }
}
