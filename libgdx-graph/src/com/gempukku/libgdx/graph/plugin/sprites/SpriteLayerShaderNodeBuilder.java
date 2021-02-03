package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpriteLayerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class SpriteLayerShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SpriteLayerShaderNodeBuilder() {
        super(new SpriteLayerShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_layer"), "a_layer", "float");

        return LibGDXCollections.singletonMap("layer", new DefaultFieldOutput(ShaderFieldType.Float, "a_layer"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_layer"), "a_layer", "float");
        if (!vertexShaderBuilder.hasVaryingVariable("v_layer")) {
            vertexShaderBuilder.addMainLine("// Sprite Layer Node");
            vertexShaderBuilder.addVaryingVariable("v_layer", "float");
            vertexShaderBuilder.addMainLine("v_layer = a_layer;");

            fragmentShaderBuilder.addVaryingVariable("v_layer", "float");
        }
        return LibGDXCollections.singletonMap("layer", new DefaultFieldOutput(ShaderFieldType.Float, "v_layer"));
    }
}
