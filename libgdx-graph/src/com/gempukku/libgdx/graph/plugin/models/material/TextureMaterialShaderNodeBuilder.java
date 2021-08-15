package com.gempukku.libgdx.graph.plugin.models.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.material.TextureMaterialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class TextureMaterialShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private String alias;

    public TextureMaterialShaderNodeBuilder(String type, String name, String alias) {
        super(new TextureMaterialShaderNodeConfiguration(type, name));
        this.alias = alias;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String textureName = "u_" + alias;
        String transformName = "u_UV" + alias;
        if (designTime) {
            Texture texture = null;
            if (data != null) {
                String previewPath = data.getString("previewPath");
                if (previewPath != null) {
                    try {
                        texture = new Texture(Gdx.files.absolute(previewPath));
                        graphShaderContext.addManagedResource(texture);
                    } catch (Exception exp) {
                        // Ignore
                    }
                }
            }
            if (texture == null)
                texture = WhitePixel.sharedInstance.texture;

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
            // Need to make sure TextureAttribute class is loaded
            TextureAttribute dummy = TextureAttribute.createAmbient(new TextureRegion());

            long attributeType = TextureAttribute.getAttributeType(alias);
            commonShaderBuilder.addUniformVariable(textureName, "sampler2D", false, new ModelsUniformSetters.MaterialTexture(attributeType));
            commonShaderBuilder.addUniformVariable(transformName, "vec4", false, new ModelsUniformSetters.MaterialTextureUV(attributeType));
        }

        return LibGDXCollections.singletonMap("texture", new DefaultFieldOutput(ShaderFieldType.TextureRegion, transformName, textureName));
    }
}
