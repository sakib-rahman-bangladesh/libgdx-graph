package com.gempukku.libgdx.graph.pipeline.producer.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.producer.BooleanFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.producer.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineRequirements;

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

        PipelineNode.FieldOutput<Boolean> processorEnabled = (PipelineNode.FieldOutput<Boolean>) inputFields.get("enabled");
        PipelineNode.FieldOutput<Float> bloomRadius = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomRadius");
        PipelineNode.FieldOutput<Float> minimalBrightness = (PipelineNode.FieldOutput<Float>) inputFields.get("minimalBrightness");
        PipelineNode.FieldOutput<Float> bloomStrength = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomStrength");
        if (processorEnabled == null)
            processorEnabled = new BooleanFieldOutput(true);
        if (bloomRadius == null)
            bloomRadius = new FloatFieldOutput(1f);
        if (minimalBrightness == null)
            minimalBrightness = new FloatFieldOutput(0.7f);
        if (bloomStrength == null)
            bloomStrength = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Boolean> finalProcessorEnabled = processorEnabled;
        final PipelineNode.FieldOutput<Float> finalBloomStrength = bloomStrength;
        final PipelineNode.FieldOutput<Float> finalBloomRadius = bloomRadius;
        final PipelineNode.FieldOutput<Float> finalMinimalBrightness = minimalBrightness;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                boolean enabled = finalProcessorEnabled.getValue(pipelineRenderingContext, null);
                float bloomStrengthValue = finalBloomStrength.getValue(pipelineRenderingContext, null);
                int bloomRadiusValue = MathUtils.round(finalBloomRadius.getValue(pipelineRenderingContext, null));
                if (enabled && bloomStrengthValue > 0 && bloomRadiusValue > 0) {
                    float minimalBrightnessValue = finalMinimalBrightness.getValue(pipelineRenderingContext, null);

                    RenderPipelineBuffer originalBuffer = renderPipeline.getDefaultBuffer();

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    RenderPipelineBuffer brightnessFilterBuffer = runBrightnessPass(minimalBrightnessValue, renderPipeline, originalBuffer, brightnessFilterPassProgram, pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());

                    RenderPipelineBuffer gaussianBlur = applyGaussianBlur(bloomRadiusValue, renderPipeline,
                            brightnessFilterBuffer, gaussianBlurPassProgram,
                            pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());
                    renderPipeline.returnFrameBuffer(brightnessFilterBuffer);

                    RenderPipelineBuffer result = applyTheBloom(bloomStrengthValue, renderPipeline, originalBuffer, gaussianBlur, bloomSumProgram, pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());

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
                bloomSumProgram.dispose();
                gaussianBlurPassProgram.dispose();
                brightnessFilterPassProgram.dispose();
            }
        };
    }

    private RenderPipelineBuffer applyTheBloom(float bloomStrength, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer, RenderPipelineBuffer gaussianBlur,
                                               ShaderProgram bloomSumProgram, RenderContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer result = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);

        result.beginColor();

        bloomSumProgram.bind();

        bloomSumProgram.setUniformi("u_brightnessTexture", renderContext.textureBinder.bind(gaussianBlur.getColorBufferTexture()));
        bloomSumProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));
        bloomSumProgram.setUniformf("u_bloomStrength", bloomStrength);

        fullScreenRender.renderFullScreen(bloomSumProgram);

        result.endColor();

        return result;
    }


    private RenderPipelineBuffer applyGaussianBlur(int bloomRadius, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                                   ShaderProgram blurProgram, RenderContext renderContext, FullScreenRender fullScreenRender) {
        float[] kernel = GaussianBlurKernel.getKernel(MathUtils.round(bloomRadius));
        blurProgram.bind();
        blurProgram.setUniformi("u_blurRadius", bloomRadius);
        blurProgram.setUniformf("u_pixelSize", 1f / sourceBuffer.getWidth(), 1f / sourceBuffer.getHeight());
        blurProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

        blurProgram.setUniformi("u_vertical", 1);
        RenderPipelineBuffer tempBuffer = executeBlur(blurProgram, renderPipeline, sourceBuffer, renderContext, fullScreenRender);
        blurProgram.setUniformi("u_vertical", 0);
        RenderPipelineBuffer blurredBuffer = executeBlur(blurProgram, renderPipeline, tempBuffer, renderContext, fullScreenRender);
        renderPipeline.returnFrameBuffer(tempBuffer);

        return blurredBuffer;
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram blurProgram, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                             RenderContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer, Color.BLACK);
        resultBuffer.beginColor();

        blurProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));

        fullScreenRender.renderFullScreen(blurProgram);

        resultBuffer.endColor();

        return resultBuffer;
    }

    private RenderPipelineBuffer runBrightnessPass(float minimalBrightnessValue, RenderPipeline renderPipeline, RenderPipelineBuffer currentBuffer, ShaderProgram brightnessFilterPassProgram,
                                                   RenderContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer brightnessFilterBuffer = renderPipeline.getNewFrameBuffer(currentBuffer, Color.BLACK);

        brightnessFilterBuffer.beginColor();

        brightnessFilterPassProgram.bind();

        brightnessFilterPassProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(currentBuffer.getColorBufferTexture()));
        brightnessFilterPassProgram.setUniformf("u_minimalBrightness", minimalBrightnessValue);

        fullScreenRender.renderFullScreen(brightnessFilterPassProgram);

        brightnessFilterBuffer.endColor();

        return brightnessFilterBuffer;
    }
}
