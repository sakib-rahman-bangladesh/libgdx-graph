package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.sprites.config.BillboardSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class BillboardSpriteShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public BillboardSpriteShaderNodeBuilder() {
        super(new BillboardSpriteShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput positionField = inputs.get("position");
        FieldOutput anchorField = inputs.get("anchor");
        FieldOutput sizeField = inputs.get("size");
        FieldOutput rotationField = inputs.get("rotation");

        loadFragmentIfNotDefined(vertexShaderBuilder, "billboardSprite");

        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(0), ShaderProgram.TEXCOORD_ATTRIBUTE + 0, "vec2");
        vertexShaderBuilder.addUniformVariable("u_cameraUp", "vec3", true, UniformSetters.cameraUp);
        vertexShaderBuilder.addUniformVariable("u_cameraDirection", "vec3", true, UniformSetters.cameraDirection);
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        if (positionField == null) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 3, "a_position"), "a_position", "vec3");
        }

        String position = (positionField != null) ? positionField.getRepresentation() : "a_position";
        String anchor = (anchorField != null) ? anchorField.getRepresentation() : "vec2(0.5, 0.5)";
        String size = (sizeField != null) ? sizeField.getRepresentation() : "vec2(100, 100)";
        String rotation = (rotationField != null) ? rotationField.getRepresentation() : "0.0";

        String name = "result_" + nodeId;

        vertexShaderBuilder.addMainLine("vec3 " + name + " = billboardSprite(" + position + ", " + size + ", " + anchor + ", " + rotation + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Billboarding of sprites is not available in fragment shader at this time");
    }
}
