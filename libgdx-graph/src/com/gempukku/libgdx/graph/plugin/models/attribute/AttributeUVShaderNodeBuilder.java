package com.gempukku.libgdx.graph.plugin.models.attribute;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.config.attribute.AttributeUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class AttributeUVShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private static Array<String> channels = new Array<String>(new String[]{"UV0", "UV1", "UV2", "UV3"});

    public AttributeUVShaderNodeBuilder() {
        super(new AttributeUVShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String channel = data.getString("channel");
        int unit = channels.indexOf(channel, false);

        String attributeName = ShaderProgram.TEXCOORD_ATTRIBUTE + unit;
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(unit), attributeName, "vec2");

        return LibGDXCollections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, attributeName));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String channel = data.getString("channel");
        int unit = channels.indexOf(channel, false);

        String attributeName = ShaderProgram.TEXCOORD_ATTRIBUTE + unit;
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(unit), attributeName, "vec2");

        String name = "v_uv_" + unit;
        if (!vertexShaderBuilder.hasVaryingVariable(name)) {
            vertexShaderBuilder.addMainLine("// Attribute UV Node");
            vertexShaderBuilder.addVaryingVariable(name, "vec2");
            vertexShaderBuilder.addMainLine(name + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(name, "vec2");
        }

        return LibGDXCollections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
    }
}
