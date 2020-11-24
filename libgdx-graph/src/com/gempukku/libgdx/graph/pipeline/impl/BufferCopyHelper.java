package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;

public class BufferCopyHelper {
    //    private SpriteBatch batch;
    private ShaderProgram shaderProgram;
    private VertexBufferObject vbo;
    private IndexBufferObject ibo;

    public BufferCopyHelper() {
//        batch = new SpriteBatch();
        shaderProgram = new ShaderProgram(
                Gdx.files.classpath("shader/draw/drawTexture.vert"),
                Gdx.files.classpath("shader/draw/drawTexture.frag")
        );
        float[] verticeData = new float[]{
                0, 0, 0,
                0, 1, 0,
                1, 0, 0,
                1, 1, 0};
        short[] indices = {0, 2, 1, 2, 3, 1};

        vbo = new VertexBufferObject(true, 4, VertexAttribute.Position());
        ibo = new IndexBufferObject(true, indices.length);
        vbo.setVertices(verticeData, 0, verticeData.length);
        ibo.setIndices(indices, 0, indices.length);
    }

    public void copy(FrameBuffer from, FrameBuffer to, RenderContext renderContext) {
        copy(from, to, renderContext, 0, 0, getBufferWidth(to), getBufferHeight(to));
    }

    public void copy(FrameBuffer from, FrameBuffer to, RenderContext renderContext, float x, float y, float width, float height) {
        if (to != null) {
            to.begin();
        }

        int bufferWidth = getBufferWidth(to);
        int bufferHeight = getBufferHeight(to);

        renderContext.setDepthTest(0);
        renderContext.setDepthMask(false);
        renderContext.setBlending(false, 0, 0);
        renderContext.setCullFace(GL20.GL_BACK);

        shaderProgram.bind();
        shaderProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(from.getColorBufferTexture()));
        shaderProgram.setUniformf("u_sourcePosition", 0, 0);
        shaderProgram.setUniformf("u_sourceSize", 1, 1);
        shaderProgram.setUniformf("u_targetPosition", x / bufferWidth, y / bufferHeight);
        shaderProgram.setUniformf("u_targetSize", width / bufferWidth, height / bufferHeight);

        vbo.bind(shaderProgram);
        ibo.bind();
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, ibo.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vbo.unbind(shaderProgram);
        ibo.unbind();

        if (to != null) {
            to.end();
        }
    }

    public void dispose() {
//        batch.dispose();
        shaderProgram.dispose();
        vbo.dispose();
        ibo.dispose();
    }

    private int getBufferWidth(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getWidth();
        return to.getWidth();
    }

    private int getBufferHeight(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getHeight();
        return to.getHeight();
    }
}
