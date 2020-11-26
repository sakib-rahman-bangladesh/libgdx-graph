package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class GaussianBlurPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GaussianBlurPipelineNodeProducer() {
        super(new GaussianBlurPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.classpath("shader/viewToScreenCoords.vert"),
                Gdx.files.classpath("shader/gaussianBlur.frag"));
        if (!shaderProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

        PipelineNode.FieldOutput<Float> blurRadius = (PipelineNode.FieldOutput<Float>) inputFields.get("blurRadius");
        if (blurRadius == null)
            blurRadius = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Float> finalBlurRadius = blurRadius;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                int blurRadius = MathUtils.round(finalBlurRadius.getValue(pipelineRenderingContext, null));
                if (blurRadius > 0) {
                    float[] kernel = GaussianBlurKernel.getKernel(blurRadius);
                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    shaderProgram.bind();
                    shaderProgram.setUniformi("u_blurRadius", blurRadius);
                    shaderProgram.setUniformf("u_pixelSize", 1f / currentBuffer.getWidth(), 1f / currentBuffer.getHeight());
                    shaderProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

                    shaderProgram.setUniformi("u_vertical", 1);
                    RenderPipelineBuffer tempBuffer = executeBlur(shaderProgram, renderPipeline, currentBuffer, pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());
                    shaderProgram.setUniformi("u_vertical", 0);
                    RenderPipelineBuffer finalBuffer = executeBlur(shaderProgram, renderPipeline, tempBuffer, pipelineRenderingContext.getRenderContext(), pipelineRenderingContext.getFullScreenRender());

                    renderPipeline.returnFrameBuffer(tempBuffer);
                    renderPipeline.swapColorTextures(currentBuffer, finalBuffer);
                    renderPipeline.returnFrameBuffer(finalBuffer);
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                shaderProgram.dispose();
            }
        };
    }

    private RenderPipelineBuffer executeBlur(ShaderProgram shaderProgram, RenderPipeline renderPipeline, RenderPipelineBuffer sourceBuffer,
                                             RenderContext renderContext, FullScreenRender fullScreenRender) {
        RenderPipelineBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer);
        resultBuffer.beginColor();

        shaderProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(sourceBuffer.getColorBufferTexture()));

        fullScreenRender.renderFullScreen(shaderProgram);

        resultBuffer.endColor();

        return resultBuffer;
    }
}
