package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.util.GdxCompatibilityUtils;

public class VertexShaderBuilder extends CommonShaderBuilder {
    private ObjectMap<String, String> attributeVariables = new OrderedMap<>();
    private ObjectMap<String, VertexAttribute> attributeVertexVariables = new OrderedMap<>();

    public VertexShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public void addAttributeVariable(VertexAttribute vertexAttribute, String type) {
        String name = vertexAttribute.alias;
        String existingType = attributeVariables.get(name);
        if (existingType != null && !existingType.equals(type))
            throw new IllegalStateException("Already contains vertex attribute of that name with different type");
        if (existingType == null) {
            uniformRegistry.registerAttribute(name);
            attributeVariables.put(name, type);
            attributeVertexVariables.put(name, vertexAttribute);
        }
    }

    private void appendAttributeVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> uniformDefinition : attributeVariables.entries()) {
            stringBuilder.append("attribute " + uniformDefinition.value + " " + uniformDefinition.key + ";\n");
        }
        if (!attributeVariables.isEmpty())
            stringBuilder.append("\n");
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        result.append(GdxCompatibilityUtils.getShaderVersionCode());
        appendInitial(result);
        appendStructures(result);
        appendAttributeVariables(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }

    public VertexAttributes getVertexAttributes() {
        return new VertexAttributes(attributeVertexVariables.values().toArray().toArray(VertexAttribute.class));
    }
}
