package com.gempukku.libgdx.graph.shader.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class SpriteGraphShader extends GraphShader {
    private int[] attributeLocations;

    public SpriteGraphShader(Texture defaultTexture) {
        super(defaultTexture);
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return propertySourceMap;
    }

    public void renderSprites(ShaderContext shaderContext, VertexAttributes vertexAttributes, SpriteData spriteData) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        if (attributeLocations == null)
            attributeLocations = getAttributeLocations(vertexAttributes);

        spriteData.render(program, attributeLocations);
    }

    private int[] getAttributeLocations(final VertexAttributes attrs) {
        IntArray tempArray = new IntArray();
        final int n = attrs.size();
        for (int i = 0; i < n; i++) {
            Attribute attribute = attributes.get(attrs.get(i).alias);
            if (attribute != null)
                tempArray.add(attribute.getLocation());
            else
                tempArray.add(-1);
        }
        return tempArray.items;
    }
}
