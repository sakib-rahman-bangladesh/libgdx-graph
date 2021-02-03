package com.gempukku.libgdx.graph.plugin.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class SpriteGraphShader extends GraphShader {
    private Array<String> textureUniformNames;

    public SpriteGraphShader(Texture defaultTexture) {
        super(defaultTexture);
        setCulling(BasicShader.Culling.none);
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return propertySourceMap;
    }

    public Array<String> getTextureUniformNames() {
        if (textureUniformNames == null) {
            textureUniformNames = new Array<>();
            for (PropertySource value : propertySourceMap.values()) {
                if (value.getShaderFieldType() == ShaderFieldType.TextureRegion)
                    textureUniformNames.add(value.getPropertyName());
            }
        }
        return textureUniformNames;
    }

    public void renderSprites(ShaderContextImpl shaderContext, SpriteData spriteData) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        spriteData.render(shaderContext, program, getAttributeLocations());
    }
}
