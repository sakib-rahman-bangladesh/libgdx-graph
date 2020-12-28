package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class SpriteSizeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SpriteSizeShaderNodeBuilder() {
        super(new SpriteSizeShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 2, "a_size"), "a_size", "vec2");

        return LibGDXCollections.singletonMap("size", new DefaultFieldOutput(ShaderFieldType.Vector2, "a_size"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 2, "a_size"), "a_size", "vec2");
        if (!vertexShaderBuilder.hasVaryingVariable("v_size")) {
            vertexShaderBuilder.addMainLine("// Sprite Size Node");
            vertexShaderBuilder.addVaryingVariable("v_size", "vec2");
            vertexShaderBuilder.addMainLine("v_size = a_size;");

            fragmentShaderBuilder.addVaryingVariable("v_size", "vec2");
        }
        return LibGDXCollections.singletonMap("size", new DefaultFieldOutput(ShaderFieldType.Float, "v_size"));
    }
}
