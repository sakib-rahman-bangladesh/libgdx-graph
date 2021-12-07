package com.gempukku.libgdx.graph.plugin.lighting3d.producer;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.lighting3d.*;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class PhongLightingShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private final int maxNumberOfDirectionalLights;
    private final int maxNumberOfPointLights;
    private final int maxNumberOfSpotlights;

    public PhongLightingShaderNodeBuilder(int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights) {
        super(new PhongLightingShaderNodeConfiguration());
        this.maxNumberOfDirectionalLights = maxNumberOfDirectionalLights;
        this.maxNumberOfPointLights = maxNumberOfPointLights;
        this.maxNumberOfSpotlights = maxNumberOfSpotlights;
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("At the moment light calculation is not available in vertex shader");
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, final GraphShaderContext graphShaderContext, GraphShader graphShader) {
        fragmentShaderBuilder.addStructure("Lighting",
                "  vec3 diffuse;\n" +
                        "  vec3 specular;\n");

        fragmentShaderBuilder.addUniformVariable("u_ambientLight_" + nodeId, "vec3", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null && environment.getAmbientColor() != null) {
                            LightColor ambientColor = environment.getAmbientColor();
                            shader.setUniform(location, ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());
                        } else {
                            shader.setUniform(location, 0f, 0f, 0f);
                        }
                    }
                });

        ObjectMap<String, String> variables = new ObjectMap<>();
        variables.put("NUM_SPOT_LIGHTS", String.valueOf(maxNumberOfSpotlights));
        variables.put("NUM_POINT_LIGHTS", String.valueOf(maxNumberOfPointLights));
        variables.put("NUM_DIRECTIONAL_LIGHTS", String.valueOf(maxNumberOfDirectionalLights));
        variables.put("NODE_ID", nodeId);

        if (maxNumberOfDirectionalLights > 0)
            passDirectionalLights(data, fragmentShaderBuilder, nodeId, variables);
        if (maxNumberOfPointLights > 0)
            passPointLights(data, fragmentShaderBuilder, nodeId, variables);
        if (maxNumberOfSpotlights > 0)
            passSpotLights(data, fragmentShaderBuilder, nodeId, variables);

        FieldOutput positionValue = inputs.get("position");
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput albedoValue = inputs.get("albedo");
        FieldOutput emissionValue = inputs.get("emission");
        FieldOutput specularValue = inputs.get("specular");
        FieldOutput ambientOcclusionValue = inputs.get("ambientOcclusion");
        FieldOutput shininessValue = inputs.get("shininess");

        String position = positionValue.getRepresentation();
        String normal = normalValue.getRepresentation();
        String albedo = albedoValue != null ? albedoValue.getRepresentation() : "vec3(0.0)";
        String emission = emissionValue != null ? emissionValue.getRepresentation() : "vec3(0.0)";
        String specular = specularValue != null ? specularValue.getRepresentation() : "vec3(1.0)";
        String ambientOcclusion = ambientOcclusionValue != null ? ambientOcclusionValue.getRepresentation() : "1.0";
        String shininess = shininessValue != null ? shininessValue.getRepresentation() : "32.0";

        fragmentShaderBuilder.addMainLine("// Phong Lighting node");
        String calculateLightingFunctionName = "calculatePhongLightingFunction_" + nodeId;
        String calculateLightingFunction = createCalculateLightingFunction(nodeId);
        fragmentShaderBuilder.addFunction(calculateLightingFunctionName, calculateLightingFunction);
        String lightingVariable = "lighting_" + nodeId;
        fragmentShaderBuilder.addMainLine("Lighting " + lightingVariable + " = " + calculateLightingFunctionName + "(" + position + ", " + normal + ", " + shininess + ");");

        ShaderFieldType resultType = ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Vector3);
        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("output")) {
            String name = "color_" + nodeId;
            fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + emission + ".rgb + " + ambientOcclusion + " * u_ambientLight_" + nodeId + " * " + albedo + ".rgb;");
            fragmentShaderBuilder.addMainLine(name + " += " + lightingVariable + ".diffuse * " + albedo + ".rgb + " + lightingVariable + ".specular * " + specular + ".rgb;");
            result.put("output", new DefaultFieldOutput(resultType.getName(), name));
        }
        if (producedOutputs.contains("diffuse")) {
            result.put("diffuse", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".diffuse"));
        }
        if (producedOutputs.contains("specularOut")) {
            result.put("specularOut", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".specular"));
        }

        return result;
    }

    private String createCalculateLightingFunction(String nodeId) {
        StringBuilder sb = new StringBuilder();

        sb.append("Lighting calculatePhongLightingFunction_" + nodeId + "(vec3 position, vec3 normal, float shininess) {\n");
        sb.append("  vec3 normalizedNormal = normalize(normal);\n");
        sb.append("  Lighting lighting = Lighting(vec3(0.0), vec3(0.0));\n");
        if (maxNumberOfDirectionalLights > 0)
            sb.append("  lighting = getDirectionalPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfPointLights > 0)
            sb.append("  lighting = getPointPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfSpotlights > 0)
            sb.append("  lighting = getSpotPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        sb.append("  return lighting;\n");
        sb.append("}\n");

        return sb.toString();
    }

    private void passSpotLights(final JsonValue data, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);
        fragmentShaderBuilder.addStructure("SpotLight",
                "  vec3 color;\n" +
                        "  vec3 position;\n" +
                        "  vec3 direction;\n" +
                        "  float cutoffAngle;\n" +
                        "  float exponent;\n");
        fragmentShaderBuilder.addStructArrayUniformVariable("u_spotLights_" + nodeId, new String[]{"color", "position", "direction", "cutoffAngle", "exponent"}, maxNumberOfSpotlights, "SpotLight", true,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Array<Spot3DLight> spots = null;
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            spots = environment.getSpotLights();
                        }

                        for (int i = 0; i < maxNumberOfSpotlights; i++) {
                            int location = startingLocation + i * structSize;
                            if (spots != null && i < spots.size) {
                                Spot3DLight spotLight = spots.get(i);
                                LightColor color = spotLight.getColor();
                                float intensity = spotLight.getIntensity();
                                Vector3 position = spotLight.getPosition();

                                shader.setUniform(location, color.getRed() * intensity,
                                        color.getGreen() * intensity, color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], position.x, position.y, position.z);
                                shader.setUniform(location + fieldOffsets[2], spotLight.getDirectionX(), spotLight.getDirectionY(),
                                        spotLight.getDirectionZ());
                                shader.setUniform(location + fieldOffsets[3], spotLight.getCutoffAngle());
                                shader.setUniform(location + fieldOffsets[4], spotLight.getExponent());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[2], 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[3], 0f);
                                shader.setUniform(location + fieldOffsets[4], 0f);
                            }
                        }
                    }
                });
        fragmentShaderBuilder.addFunction("getSpotPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment("phong/spotLightContribution", variables));
    }

    private void passPointLights(final JsonValue data, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);
        fragmentShaderBuilder.addStructure("PointLight",
                "  vec3 color;\n" +
                        "  vec3 position;\n");
        fragmentShaderBuilder.addStructArrayUniformVariable("u_pointLights_" + nodeId, new String[]{"color", "position"}, maxNumberOfPointLights, "PointLight", true,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Array<Point3DLight> points = null;
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            points = environment.getPointLights();
                        }

                        for (int i = 0; i < maxNumberOfPointLights; i++) {
                            int location = startingLocation + i * structSize;
                            if (points != null && i < points.size) {
                                Point3DLight pointLight = points.get(i);
                                LightColor color = pointLight.getColor();
                                float intensity = pointLight.getIntensity();
                                Vector3 position = pointLight.getPosition();

                                shader.setUniform(location, color.getRed() * intensity,
                                        color.getGreen() * intensity, color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], position.x, position.y, position.z);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                            }
                        }
                    }
                });
        fragmentShaderBuilder.addFunction("getPointPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment("phong/pointLightContribution", variables));
    }

    private void passDirectionalLights(final JsonValue data, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);
        fragmentShaderBuilder.addStructure("DirectionalLight",
                "  vec3 color;\n" +
                        "  vec3 direction;\n");
        fragmentShaderBuilder.addStructArrayUniformVariable("u_dirLights_" + nodeId, new String[]{"color", "direction"}, maxNumberOfDirectionalLights, "DirectionalLight", true,
                new UniformRegistry.StructArrayUniformSetter() {
                    @Override
                    public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext) {
                        Array<Directional3DLight> dirs = null;
                        Lighting3DEnvironment environment = shaderContext.getPrivatePluginData(Lighting3DPrivateData.class).getEnvironment(data.getString("id", ""));
                        if (environment != null) {
                            dirs = environment.getDirectionalLights();
                        }

                        for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
                            int location = startingLocation + i * structSize;
                            if (dirs != null && i < dirs.size) {
                                Directional3DLight directionalLight = dirs.get(i);
                                LightColor color = directionalLight.getColor();
                                float intensity = directionalLight.getIntensity();

                                shader.setUniform(location, color.getRed() * intensity, color.getGreen() * intensity,
                                        color.getBlue() * intensity);
                                shader.setUniform(location + fieldOffsets[1], directionalLight.getDirectionX(),
                                        directionalLight.getDirectionY(), directionalLight.getDirectionZ());
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                                shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                            }
                        }
                    }
                });
        fragmentShaderBuilder.addFunction("getDirectionalPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment("phong/directionalLightContribution", variables));
    }
}
