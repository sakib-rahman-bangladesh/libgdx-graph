package com.gempukku.libgdx.graph.shader.particles.particle;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleSeedShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ParticleSeedShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ParticleSeedShaderNodeBuilder() {
        super(new ParticleSeedShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_seed"), "a_seed", "float");

        return LibGDXCollections.singletonMap("seed", new DefaultFieldOutput(ShaderFieldType.Float, "a_seed"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_seed"), "a_seed", "float");

        if (!vertexShaderBuilder.hasVaryingVariable("v_seed")) {
            vertexShaderBuilder.addMainLine("// Particle Seed Node");
            vertexShaderBuilder.addVaryingVariable("v_seed", "float");
            vertexShaderBuilder.addMainLine("v_seed = a_seed;");

            fragmentShaderBuilder.addVaryingVariable("v_seed", "float");
        }
        return LibGDXCollections.singletonMap("seed", new DefaultFieldOutput(ShaderFieldType.Float, "v_seed"));
    }
}
