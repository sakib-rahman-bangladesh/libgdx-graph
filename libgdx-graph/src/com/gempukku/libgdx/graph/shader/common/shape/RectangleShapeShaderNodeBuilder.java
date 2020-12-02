package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.RectangleShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class RectangleShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RectangleShapeShaderNodeBuilder() {
        super(new RectangleShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput sizeValue = inputs.get("size");
        FieldOutput widthValue = inputs.get("width");

        String uv = uvValue.getRepresentation();
        String size = sizeValue != null ? sizeValue.getRepresentation() : "vec2(1.0)";
        String width = widthValue != null ? widthValue.getRepresentation() : "1.0";

        commonShaderBuilder.addMainLine("// Rectangle shape node");
        String temp1 = "temp_" + nodeId;
        String name = "result_" + nodeId;
        ShaderFieldType resultType = ShaderFieldType.Float;

        commonShaderBuilder.addMainLine("vec2 " + temp1 + " = abs(" + uv + " * 2.0 - 1.0);");
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = step(0.0, min(" + size + ".x - " + temp1 + ".x, " + size + ".y - " + temp1 + ".y)) - step(0.0, min(" + size + ".x - " + width + " * 2.0 - " + temp1 + ".x, " + size + ".y - " + width + " * 2.0 - " + temp1 + ".y));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
