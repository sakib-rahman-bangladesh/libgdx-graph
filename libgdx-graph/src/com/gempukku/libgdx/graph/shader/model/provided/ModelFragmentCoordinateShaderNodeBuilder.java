package com.gempukku.libgdx.graph.shader.model.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.ModelFragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ModelFragmentCoordinateShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ModelFragmentCoordinateShaderNodeBuilder() {
        super(new ModelFragmentCoordinateShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addUniformVariable("u_viewportSize", "vec2", true, UniformSetters.viewportSize);
        commonShaderBuilder.addUniformVariable("u_pixelSize", "vec2", true, UniformSetters.pixelSize);
        commonShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        commonShaderBuilder.addUniformVariable("u_viewTrans", "mat4", true, UniformSetters.viewTrans);
        commonShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);

        String tmpName = "tmp_" + nodeId;
        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Model Fragment Coordinate Node");
        commonShaderBuilder.addMainLine("vec4 " + tmpName + " = u_projViewTrans * u_worldTrans * vec4(0.0, 0.0, 0.0, 1.0);");
        commonShaderBuilder.addMainLine("vec2 " + resultName + " = u_viewportSize * (" + tmpName + ".xy / " + tmpName + ".w + 1.0) / 2.0;");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector2, resultName));
    }
}
