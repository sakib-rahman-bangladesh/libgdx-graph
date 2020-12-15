package com.gempukku.libgdx.graph.shader.particles.particle;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleRandomShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;

public class ParticleRandomShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private float multiplier;

    public ParticleRandomShaderNodeBuilder() {
        super(new ParticleRandomShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        multiplier = MathUtils.random(1000f, 10000f);

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_seed"), "a_seed", "float");

        float min = data.getFloat("min");
        float max = data.getFloat("max");

        String minValue = SimpleNumberFormatter.format(min);
        String maxValue = SimpleNumberFormatter.format(max);
        String multiplierValue = SimpleNumberFormatter.format(multiplier);

        String name = "result_" + nodeId;
        vertexShaderBuilder.addMainLine("// Particle Random Node");
        vertexShaderBuilder.addMainLine("float" + " " + name + " = " + minValue + " + (" + maxValue + " - " + minValue + ") * fract(a_seed * " + multiplierValue + ");");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        multiplier = MathUtils.random(1000f, 10000f);

        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_seed"), "a_seed", "float");

        if (!vertexShaderBuilder.hasVaryingVariable("v_seed")) {
            vertexShaderBuilder.addMainLine("// Particle Seed Node");
            vertexShaderBuilder.addVaryingVariable("v_seed", "float");
            vertexShaderBuilder.addMainLine("v_seed = a_seed;");

            fragmentShaderBuilder.addVaryingVariable("v_seed", "float");
        }

        float min = data.getFloat("min");
        float max = data.getFloat("max");

        String minValue = SimpleNumberFormatter.format(min);
        String maxValue = SimpleNumberFormatter.format(max);
        String multiplierValue = SimpleNumberFormatter.format(multiplier);

        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine("// Particle Random Node");
        fragmentShaderBuilder.addMainLine("float" + " " + name + " = " + minValue + " + (" + maxValue + " - " + minValue + ") * fract(v_seed * " + multiplierValue + ");");

        return LibGDXCollections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
