package com.gempukku.libgdx.graph.shader.node.noise;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.noise.VoronoiBorder2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class VoronoiBorder2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public VoronoiBorder2DShaderNodeBuilder() {
        super(new VoronoiBorder2DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput progressValue = inputs.get("progress");

        String scale = (scaleValue != null) ? scaleValue.getRepresentation() : "1.0";
        String progress = (progressValue != null) ? progressValue.getRepresentation() : "0.0";

        if (!commonShaderBuilder.containsFunction("voronoiBorder2d")) {
            commonShaderBuilder.addFunction("voronoiBorder2d", GLSLFragmentReader.getFragment("voronoiBorder2d"));
        }

        commonShaderBuilder.addMainLine("// Voronoi border 2D node");
        String name = "result_" + nodeId;
        String output;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            output = "voronoiBorder2d(" + uvValue.getRepresentation() + " * " + scale + ", " + progress + ")";
        } else {
            output = "voronoiBorder2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scale + ", " + progress + ")";
        }

        commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
