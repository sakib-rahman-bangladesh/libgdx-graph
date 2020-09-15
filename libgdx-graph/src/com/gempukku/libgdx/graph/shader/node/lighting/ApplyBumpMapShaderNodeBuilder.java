package com.gempukku.libgdx.graph.shader.node.lighting;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.lighting.ApplyBumpMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ApplyBumpMapShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ApplyBumpMapShaderNodeBuilder() {
        super(new ApplyBumpMapShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addMainLine("// Apply bump map");
        if (!commonShaderBuilder.containsFunction("applyBumpMap")) {
            commonShaderBuilder.addFunction("applyBumpMap", GLSLFragmentReader.getFragment("applyBumpMap"));
        }

        FieldOutput normal = inputs.get("normal");
        FieldOutput tangent = inputs.get("tangent");
        FieldOutput bump = inputs.get("bump");

        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine(ShaderFieldType.Vector3.getShaderType() + " " + name + " = applyBumpMap(" + tangent.getRepresentation() + ", " + normal.getRepresentation() + ", " + bump.getRepresentation() + ".xyz);");
        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
    }
}
