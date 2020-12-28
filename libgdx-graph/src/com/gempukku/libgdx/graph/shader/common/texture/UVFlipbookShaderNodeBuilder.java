package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class UVFlipbookShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public UVFlipbookShaderNodeBuilder() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput tileCountValue = inputs.get("tileCount");
        FieldOutput indexValue = inputs.get("index");
        FieldOutput loopingValue = inputs.get("looping");
        if (loopingValue == null) {
            loopingValue = new DefaultFieldOutput(ShaderFieldType.Float, "1.0");
        }

        loadFragmentIfNotDefined(commonShaderBuilder, "uvFlipbook");

        commonShaderBuilder.addMainLine("// UV Flipbook Node");
        boolean invertX = data.getBoolean("invertX");
        boolean invertY = data.getBoolean("invertY");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("vec2 " + resultName + " = uvFlipbook(" + uvValue + ", " + tileCountValue + ".x, " + tileCountValue + ".y, " + indexValue + ", " + loopingValue + ", vec2(" + (invertX ? "1.0" : "0.0") + ", " + (invertY ? "1.0" : "0.0") + "));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector2, resultName));
    }
}
