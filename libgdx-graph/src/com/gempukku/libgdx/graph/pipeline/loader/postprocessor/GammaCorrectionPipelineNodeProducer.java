package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineBuffer;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineRequirements;

public class GammaCorrectionPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GammaCorrectionPipelineNodeProducer() {
        super(new GammaCorrectionPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNodeForSingleInputs(JsonValue data, ObjectMap<String, PipelineNode.FieldOutput<?>> inputFields) {
        final ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.classpath("shader/viewToScreenCoords.vert"),
                Gdx.files.classpath("shader/gamma.frag"));
        if (!shaderProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());


        PipelineNode.FieldOutput<Float> gamma = (PipelineNode.FieldOutput<Float>) inputFields.get("gamma");
        if (gamma == null)
            gamma = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Float> finalGamma = gamma;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, PipelineRequirements pipelineRequirements, ObjectMap<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext, pipelineRequirements);

                float gamma = finalGamma.getValue(pipelineRenderingContext, null);
                if (gamma != 1) {
                    RenderPipelineBuffer currentBuffer = renderPipeline.getDefaultBuffer();

                    RenderPipelineBuffer newBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);

                    RenderContext renderContext = pipelineRenderingContext.getRenderContext();
                    renderContext.setDepthTest(0);
                    renderContext.setDepthMask(false);
                    renderContext.setBlending(false, 0, 0);
                    renderContext.setCullFace(GL20.GL_BACK);

                    newBuffer.beginColor();

                    shaderProgram.bind();

                    shaderProgram.setUniformi("u_sourceTexture", renderContext.textureBinder.bind(currentBuffer.getColorBufferTexture()));
                    shaderProgram.setUniformf("u_gamma", gamma);

                    pipelineRenderingContext.getFullScreenRender().renderFullScreen(shaderProgram);

                    newBuffer.endColor();

                    renderPipeline.swapColorTextures(currentBuffer, newBuffer);
                    renderPipeline.returnFrameBuffer(newBuffer);
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
}
