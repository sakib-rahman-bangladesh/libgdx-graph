package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.loader.FullScreenRender;

public class FullScreenRenderImpl implements FullScreenRender, Disposable {
    private VertexBufferObject vertexBufferObject;
    private IndexBufferObject indexBufferObject;

    public FullScreenRenderImpl() {
        float[] verticeData = new float[]{
                0, 0, 0,
                0, 1, 0,
                1, 0, 0,
                1, 1, 0};
        short[] indices = {0, 2, 1, 2, 3, 1};

        vertexBufferObject = new VertexBufferObject(true, 4, VertexAttribute.Position());
        indexBufferObject = new IndexBufferObject(true, indices.length);
        vertexBufferObject.setVertices(verticeData, 0, verticeData.length);
        indexBufferObject.setIndices(indices, 0, indices.length);
    }

    @Override
    public void renderFullScreen(ShaderProgram shaderProgram) {
        vertexBufferObject.bind(shaderProgram);
        indexBufferObject.bind();
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(shaderProgram);
        indexBufferObject.unbind();
    }

    @Override
    public void dispose() {
        vertexBufferObject.dispose();
        indexBufferObject.dispose();
    }
}
