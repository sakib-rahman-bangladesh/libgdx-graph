package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpriteUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class SpriteUVShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SpriteUVShaderNodeBuilder() {
        super(new SpriteUVShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(0), ShaderProgram.TEXCOORD_ATTRIBUTE + 0, "vec3");

        return LibGDXCollections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, "a_texCoord0"));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(0), ShaderProgram.TEXCOORD_ATTRIBUTE + 0, "vec2");
        if (!vertexShaderBuilder.hasVaryingVariable("v_uv")) {
            vertexShaderBuilder.addMainLine("// Sprite UV Node");
            vertexShaderBuilder.addVaryingVariable("v_uv", "vec2");
            vertexShaderBuilder.addMainLine("v_uv = a_texCoord0;");

            fragmentShaderBuilder.addVaryingVariable("v_uv", "vec2");
        }
        return LibGDXCollections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, "v_uv"));
    }
}
