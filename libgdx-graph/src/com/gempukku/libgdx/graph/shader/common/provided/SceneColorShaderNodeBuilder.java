package com.gempukku.libgdx.graph.shader.common.provided;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.WhitePixel;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SceneColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SceneColorShaderNodeBuilder() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        graphShader.setUsingColorTexture(true);
        String textureName = "u_" + nodeId;
        String transformName = "u_UV" + nodeId;
        if (designTime) {
            Texture texture = WhitePixel.sharedInstance.texture;

            final Texture finalTexture = texture;
            commonShaderBuilder.addUniformVariable(textureName, "sampler2D", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            shader.setUniform(location, finalTexture);
                        }
                    });
            commonShaderBuilder.addUniformVariable(transformName, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            shader.setUniform(location, 0f, 0f, 1f, 1f);
                        }
                    });
        } else {
            final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
            if (data.has("minFilter"))
                textureDescriptor.minFilter = Texture.TextureFilter.valueOf(data.getString("minFilter"));
            if (data.has("magFilter"))
                textureDescriptor.magFilter = Texture.TextureFilter.valueOf(data.getString("magFilter"));
            if (data.has("uWrap"))
                textureDescriptor.uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
            if (data.has("vWrap"))
                textureDescriptor.vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

            commonShaderBuilder.addUniformVariable(textureName, "sampler2D", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            textureDescriptor.texture = shaderContext.getColorTexture();
                            shader.setUniform(location, textureDescriptor);
                        }
                    });
            commonShaderBuilder.addUniformVariable(transformName, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            shader.setUniform(location, 0f, 0f, 1f, 1f);
                        }
                    });
        }

        return LibGDXCollections.singletonMap("texture", new DefaultFieldOutput(ShaderFieldType.TextureRegion, transformName, textureName));
    }
}
