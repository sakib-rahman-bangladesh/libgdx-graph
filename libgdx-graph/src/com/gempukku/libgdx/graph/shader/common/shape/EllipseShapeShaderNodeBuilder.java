package com.gempukku.libgdx.graph.shader.common.shape;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.shape.EllipseShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class EllipseShapeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public EllipseShapeShaderNodeBuilder() {
        super(new EllipseShapeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput sizeValue = inputs.get("size");
        FieldOutput widthValue = inputs.get("width");

        String uv = uvValue.getRepresentation();
        String size = sizeValue != null ? sizeValue.getRepresentation() : "vec2(1.0)";
        String width = widthValue != null ? widthValue.getRepresentation() : "1.0";

        commonShaderBuilder.addMainLine("// Ellipse shape node");
        String temp1 = "temp_" + nodeId;
        String name = "result_" + nodeId;
        ShaderFieldType resultType = ShaderFieldType.Float;

        commonShaderBuilder.addMainLine("vec2 " + temp1 + " = " + uv + " * 2.0 - 1.0;");
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = step(1.0, length(" + temp1 + " / max(" + size + " - 2.0 * " + width + ", 0.0000001))) - step(1.0, length(" + temp1 + " / " + size + "));");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
