package com.gempukku.libgdx.graph.plugin.particles.particle;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ParticleLifetimeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ParticleLifetimeShaderNodeBuilder() {
        super(new ParticleLifetimeShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, "a_birthTime"), "float");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time);

        String name = "result_" + nodeId;
        vertexShaderBuilder.addMainLine("// Particle Lifetime Node");
        vertexShaderBuilder.addMainLine("float" + " " + name + " = u_time - a_birthTime;");

        return LibGDXCollections.singletonMap("time", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        VertexAttribute attribute = new VertexAttribute(1024, 1, "a_birthTime");
        vertexShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time);
        copyAttributeToFragmentShader(attribute, "v_lifetime", "u_time - a_birthTime",
                vertexShaderBuilder, fragmentShaderBuilder);

        return LibGDXCollections.singletonMap("time", new DefaultFieldOutput(ShaderFieldType.Float, "v_lifetime"));
    }
}
