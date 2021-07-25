package com.gempukku.libgdx.graph.plugin.particles.particle;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLocationShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ParticleLocationShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ParticleLocationShaderNodeBuilder() {
        super(new ParticleLocationShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");

        return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "a_position"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");
        if (!vertexShaderBuilder.hasVaryingVariable("v_position")) {
            vertexShaderBuilder.addMainLine("// Particle Location Node");
            vertexShaderBuilder.addVaryingVariable("v_position", "vec3");
            vertexShaderBuilder.addMainLine("v_position = a_position;");

            fragmentShaderBuilder.addVaryingVariable("v_position", "vec3");
        }
        return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position"));
    }
}
