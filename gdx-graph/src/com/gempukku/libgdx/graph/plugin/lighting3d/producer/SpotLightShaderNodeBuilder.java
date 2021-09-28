package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.lighting3d.LightColor;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Spot3DLight;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SpotLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SpotLightShaderNodeBuilder() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final int index = data.getInt("index");

        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("position")) {
            String name = "u_spotLightPosition" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getPosition());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("direction")) {
            String name = "u_spotLightDirection" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getDirectionX(), spotLight.getDirectionY(), spotLight.getDirectionZ());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_spotLightColor" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                LightColor color = spotLight.getColor();
                                shader.setUniform(location, color.getRed(), color.getGreen(), color.getBlue(), 1f);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    });
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Vector4, name));
        }
        if (producedOutputs.contains("intensity")) {
            String name = "u_spotLightIntensity" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getIntensity());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("intensity", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("cutOffAngle")) {
            String name = "u_spotLightCutOffAngle" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getCutoffAngle());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("cutOffAngle", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("exponent")) {
            String name = "u_spotLightExponent" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                            Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<Spot3DLight> spotLights = environment.getSpotLights();
                                Spot3DLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.getExponent());
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("exponent", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
