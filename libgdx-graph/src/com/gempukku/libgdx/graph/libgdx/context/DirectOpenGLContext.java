package com.gempukku.libgdx.graph.libgdx.context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;

public class DirectOpenGLContext implements OpenGLContext {
    @Override
    public void setDepthMask(boolean depthMask) {
        Gdx.gl.glDepthMask(depthMask);
    }

    @Override
    public void setDepthTest(int depthFunction) {
        setDepthTest(depthFunction, 0f, 1f);
    }

    @Override
    public void setDepthTest(int depthFunction, float depthRangeNear, float depthRangeFar) {
        boolean enabled = depthFunction != 0;
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
            Gdx.gl.glDepthFunc(depthFunction);
            Gdx.gl.glDepthRangef(depthRangeNear, depthRangeFar);
        } else {
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        }
    }

    @Override
    public void setBlending(boolean enabled, int sFactor, int dFactor) {
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(sFactor, dFactor);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void setBlendingSeparate(boolean enabled, int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha) {
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFuncSeparate(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void setCullFace(int face) {
        boolean enabled = face != 0;
        if (enabled) {
            Gdx.gl.glEnable(GL20.GL_CULL_FACE);
            Gdx.gl.glCullFace(face);
        } else {
            Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        }
    }

    @Override
    public int bind(TextureDescriptor<?> textureDescriptor, int defaultUnit) {
        GLTexture texture = textureDescriptor.texture;
        texture.bind(defaultUnit);
        texture.unsafeSetWrap(textureDescriptor.uWrap, textureDescriptor.vWrap);
        texture.unsafeSetFilter(textureDescriptor.minFilter, textureDescriptor.magFilter);
        return defaultUnit;
    }

    @Override
    public int bind(GLTexture texture, int defaultUnit) {
        texture.bind(defaultUnit);
        return defaultUnit;
    }
}
