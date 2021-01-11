package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexBufferObjectWithVAO;
import com.badlogic.gdx.graphics.glutils.VertexData;

public class GdxCompatibilityUtils {
    public static VertexData createVertexBuffer(boolean isStatic, int numVertices, VertexAttributes attributes) {
        if (Gdx.gl30 != null)
            return new VertexBufferObjectWithVAO(isStatic, numVertices, attributes);
        else
            return new VertexBufferObject(isStatic, numVertices, attributes);
    }

    public static String getShaderPrefixCode() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            return "#version 120\n";
        else
            return "#version 100\n";
    }
}
