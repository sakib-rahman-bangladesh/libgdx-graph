package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.util.WhitePixel;

public abstract class TextureShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.TextureRegion;
    }

    @Override
    public PropertySource createProperty(int index, String name, JsonValue data, boolean designTime) {
        if (designTime)
            return new PropertySource(index, name, ShaderFieldType.TextureRegion, WhitePixel.sharedInstance.textureRegion);
        else
            return new PropertySource(index, name, ShaderFieldType.TextureRegion, getDefaultTextureRegion());
    }

    protected abstract TextureRegion getDefaultTextureRegion();
}
