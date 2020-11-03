package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SceneColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SceneColorShaderNodeBuilder() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        graphShader.setUsingColorTexture(true);
        if (designTime) {
            return LibGDXCollections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Color, "vec4(1.0)"));
        } else {
            commonShaderBuilder.addUniformVariable("u_sceneColorTexture", "sampler2D", true, UniformSetters.colorTexture);

            FieldOutput screenPosition = inputs.get("screenPosition");
            String screenPositionValue = screenPosition != null ? screenPosition.getRepresentation() : "gl_FragCoord";
            String name = "depth_" + nodeId;
            commonShaderBuilder.addMainLine("// Scene color node");
            commonShaderBuilder.addMainLine("vec4 " + name + " = texture2D(u_sceneColorTexture, " + screenPositionValue + ".xy);");
            return LibGDXCollections.singletonMap("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
    }
}
