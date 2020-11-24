package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.DepthOfFieldPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;

import java.nio.charset.StandardCharsets;

public class DepthOfFieldPipelineNodeProducer extends PipelineNodeProducerImpl {
    public DepthOfFieldPipelineNodeProducer() {
        super(new DepthOfFieldPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        float maxBlurFloat = data.getFloat("maxBlur");
        final int maxBlur = MathUtils.round(maxBlurFloat);
        boolean blurBackground = data.getBoolean("blurBackground", false);

        String viewToScreenCoords = getShader("viewToScreenCoords.vert");
        String depthOfField = getShader("depthOfField.frag");
        depthOfField = depthOfField.replaceAll("MAX_BLUR", String.valueOf(maxBlur));
        depthOfField = depthOfField.replaceAll("UNPACK_FUNCTION", GLSLFragmentReader.getFragment("unpackVec3ToFloat"));
        depthOfField = depthOfField.replaceAll("BLUR_BACKGROUND", String.valueOf(blurBackground));

        final ShaderProgram shaderProgram = new ShaderProgram(
                viewToScreenCoords, depthOfField);
        if (!shaderProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

        float[] verticeData = new float[]{
                0, 0, 0,
                0, 1, 0,
                1, 0, 0,
                1, 1, 0};
        short[] indices = {0, 2, 1, 2, 3, 1};

        final VertexBufferObject vertexBufferObject = new VertexBufferObject(true, 4, VertexAttribute.Position());
        final IndexBufferObject indexBufferObject = new IndexBufferObject(true, indices.length);
        vertexBufferObject.setVertices(verticeData, 0, verticeData.length);
        indexBufferObject.setIndices(indices, 0, indices.length);

        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<Vector2> focusDistanceInput = (PipelineNode.FieldOutput<Vector2>) inputFields.get("focusDistance");
        final PipelineNode.FieldOutput<Float> nearDistanceBlurInput = (PipelineNode.FieldOutput<Float>) inputFields.get("nearDistanceBlur");
        final PipelineNode.FieldOutput<Float> farDistanceBlurInput = (PipelineNode.FieldOutput<Float>) inputFields.get("farDistanceBlur");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                if (maxBlur > 0)
                    pipelineRequirements.setRequiringDepthTexture();

                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                if (maxBlur > 0) {
                    Camera camera = cameraInput.getValue(pipelineRenderingContext, null);
                    Vector2 focusDistance = focusDistanceInput.getValue(pipelineRenderingContext, null);
                    float nearDistanceBlur = nearDistanceBlurInput != null ? nearDistanceBlurInput.getValue(pipelineRenderingContext, null) : 10f;
                    float farDistanceBlur = farDistanceBlurInput != null ? farDistanceBlurInput.getValue(pipelineRenderingContext, null) : 10f;

                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    shaderProgram.bind();
                    shaderProgram.setUniformf("u_pixelSize", 1f / currentBuffer.getWidth(), 1f / currentBuffer.getHeight());
                    shaderProgram.setUniformf("u_cameraClipping", camera.near, camera.far);
                    shaderProgram.setUniformf("u_focusDistance", focusDistance);
                    shaderProgram.setUniformf("u_nearDistanceBlur", nearDistanceBlur);
                    shaderProgram.setUniformf("u_farDistanceBlur", farDistanceBlur);

                    vertexBufferObject.bind(shaderProgram);
                    indexBufferObject.bind();

                    shaderProgram.setUniformi("u_vertical", 1);
                    RenderPipelineBuffer tempBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, currentBuffer, indexBufferObject, pipelineRenderingContext.getRenderContext());
                    shaderProgram.setUniformi("u_vertical", 0);
                    RenderPipelineBuffer finalBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, tempBuffer, indexBufferObject, pipelineRenderingContext.getRenderContext());
                    renderPipeline.returnFrameBuffer(tempBuffer);
                    renderPipeline.swapColorTextures(currentBuffer, finalBuffer);
                    renderPipeline.returnFrameBuffer(finalBuffer);

                    indexBufferObject.unbind();
                    vertexBufferObject.unbind(shaderProgram);
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                vertexBufferObject.dispose();
                indexBufferObject.dispose();
                shaderProgram.dispose();
            }
        };
    }

    private String getShader(String shaderName) {
        FileHandle fileHandle = Gdx.files.classpath("shader/" + shaderName);
        return new String(fileHandle.readBytes(), StandardCharsets.UTF_8);
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram shaderProgram, RenderPipeline renderPipeline, RenderPipelineBuffer depthBuffer, RenderPipelineBuffer sourceBuffer, IndexBufferObject indexBufferObject,
                                             RenderContext renderContext) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer);
        resultBuffer.beginColor();

        shaderProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));
        shaderProgram.setUniformi("u_depthTexture", renderContext.textureBinder.bind(depthBuffer.getDepthBufferTexture()));

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);

        resultBuffer.endColor();

        return resultBuffer;
    }
}
