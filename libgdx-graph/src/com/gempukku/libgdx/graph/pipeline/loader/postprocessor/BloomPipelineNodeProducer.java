package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class BloomPipelineNodeProducer extends PipelineNodeProducerImpl {
    public BloomPipelineNodeProducer() {
        super(new BloomPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final ShaderProgram brightnessFilterPassProgram = new ShaderProgram(
                Gdx.files.classpath("shader/viewToScreenCoords.vert"),
                Gdx.files.classpath("shader/brightnessFilter.frag"));
        if (!brightnessFilterPassProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + brightnessFilterPassProgram.getLog());
        final ShaderProgram gaussianBlurPassProgram = new ShaderProgram(
                Gdx.files.classpath("shader/viewToScreenCoords.vert"),
                Gdx.files.classpath("shader/gaussianBlur.frag"));
        if (!gaussianBlurPassProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + gaussianBlurPassProgram.getLog());
        final ShaderProgram bloomSumProgram = new ShaderProgram(
                Gdx.files.classpath("shader/viewToScreenCoords.vert"),
                Gdx.files.classpath("shader/bloomSum.frag"));
        if (!bloomSumProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + bloomSumProgram.getLog());

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

        PipelineNode.FieldOutput<Float> bloomRadius = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomRadius");
        PipelineNode.FieldOutput<Float> minimalBrightness = (PipelineNode.FieldOutput<Float>) inputFields.get("minimalBrightness");
        PipelineNode.FieldOutput<Float> bloomStrength = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomStrength");
        if (bloomRadius == null)
            bloomRadius = new FloatFieldOutput(1f);
        if (minimalBrightness == null)
            minimalBrightness = new FloatFieldOutput(0.7f);
        if (bloomStrength == null)
            bloomStrength = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Float> finalBloomStrength = bloomStrength;
        final PipelineNode.FieldOutput<Float> finalBloomRadius = bloomRadius;
        final PipelineNode.FieldOutput<Float> finalMinimalBrightness = minimalBrightness;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                float bloomStrengthValue = finalBloomStrength.getValue(pipelineRenderingContext, null);
                int bloomRadiusValue = MathUtils.round(finalBloomRadius.getValue(pipelineRenderingContext, null));
                if (bloomStrengthValue > 0 && bloomRadiusValue > 0) {
                    float minimalBrightnessValue = finalMinimalBrightness.getValue(pipelineRenderingContext, null);

                    RenderPipelineBuffer originalBuffer = renderPipeline.getDefaultBuffer();

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    RenderPipelineBuffer brightnessFilterBuffer = runBrightnessPass(minimalBrightnessValue, renderPipeline, originalBuffer, brightnessFilterPassProgram, vertexBufferObject, indexBufferObject,
                            pipelineRenderingContext.getRenderContext());

                    RenderPipelineBuffer gaussianBlur = applyGaussianBlur(bloomRadiusValue, renderPipeline,
                            brightnessFilterBuffer, gaussianBlurPassProgram, vertexBufferObject, indexBufferObject,
                            pipelineRenderingContext.getRenderContext());
                    renderPipeline.returnFrameBuffer(brightnessFilterBuffer);

                    RenderPipelineBuffer result = applyTheBloom(bloomStrengthValue, renderPipeline, originalBuffer, gaussianBlur, bloomSumProgram, vertexBufferObject, indexBufferObject,
                            pipelineRenderingContext.getRenderContext());

                    renderPipeline.returnFrameBuffer(gaussianBlur);

                    renderPipeline.swapColorTextures(originalBuffer, result);
                    renderPipeline.returnFrameBuffer(result);
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                vertexBufferObject.dispose();
                indexBufferObject.dispose();
                bloomSumProgram.dispose();
                gaussianBlurPassProgram.dispose();
                brightnessFilterPassProgram.dispose();
            }
        };
    }

    private RenderPipelineBuffer applyTheBloom(float bloomStrength, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer, RenderPipelineBuffer gaussianBlur,
                                               ShaderProgram bloomSumProgram, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject,
                                               RenderContext renderContext) {
        RenderPipelineBuffer result = renderPipeline.getNewFrameBuffer(sourceBuffer);

        result.beginColor();

        bloomSumProgram.bind();

        vertexBufferObject.bind(bloomSumProgram);
        indexBufferObject.bind();

        bloomSumProgram.setUniformi("u_brightnessTexture", renderContext.textureBinder.bind(gaussianBlur.getColorBufferTexture()));
        bloomSumProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));
        bloomSumProgram.setUniformf("u_bloomStrength", bloomStrength);

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(bloomSumProgram);
        indexBufferObject.unbind();

        result.endColor();

        return result;
    }


    private RenderPipelineBuffer applyGaussianBlur(int bloomRadius, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                                   ShaderProgram blurProgram, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject,
                                                   RenderContext renderContext) {
        float[] kernel = GaussianBlurKernel.getKernel(MathUtils.round(bloomRadius));
        blurProgram.bind();
        blurProgram.setUniformi("u_blurRadius", bloomRadius);
        blurProgram.setUniformf("u_pixelSize", 1f / sourceBuffer.getWidth(), 1f / sourceBuffer.getHeight());
        blurProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

        vertexBufferObject.bind(blurProgram);
        indexBufferObject.bind();

        blurProgram.setUniformi("u_vertical", 1);
        RenderPipelineBuffer tempBuffer = executeBlur(blurProgram, renderPipeline, sourceBuffer, indexBufferObject, renderContext);
        blurProgram.setUniformi("u_vertical", 0);
        RenderPipelineBuffer blurredBuffer = executeBlur(blurProgram, renderPipeline, tempBuffer, indexBufferObject, renderContext);
        renderPipeline.returnFrameBuffer(tempBuffer);

        indexBufferObject.unbind();
        vertexBufferObject.unbind(blurProgram);

        return blurredBuffer;
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram blurProgram, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer, IndexBufferObject indexBufferObject,
                                             RenderContext renderContext) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer);
        resultBuffer.beginColor();

        blurProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);

        resultBuffer.endColor();

        return resultBuffer;
    }

    private RenderPipelineBuffer runBrightnessPass(float minimalBrightnessValue, RenderPipeline renderPipeline, RenderPipelineBuffer currentBuffer, ShaderProgram brightnessFilterPassProgram,
                                                   VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject, RenderContext renderContext) {
        RenderPipelineBuffer brightnessFilterBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);

        brightnessFilterBuffer.beginColor();

        brightnessFilterPassProgram.bind();

        vertexBufferObject.bind(brightnessFilterPassProgram);
        indexBufferObject.bind();

        brightnessFilterPassProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(currentBuffer.getColorBufferTexture()));
        brightnessFilterPassProgram.setUniformf("u_minimalBrightness", minimalBrightnessValue);

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(brightnessFilterPassProgram);
        indexBufferObject.unbind();

        brightnessFilterBuffer.endColor();

        return brightnessFilterBuffer;
    }
}
