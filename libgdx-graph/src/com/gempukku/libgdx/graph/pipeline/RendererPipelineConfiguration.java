package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.loader.math.AddPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.math.MultiplyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.math.SubtractPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.part.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.part.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.DepthOfFieldPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.GaussianBlurPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.provided.RenderSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.provided.TimePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.CustomRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ModelShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.PipelineRendererNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.ScreenShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.SpriteShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueBooleanPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueColorPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueFloatPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueVector2PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueVector3PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.property.BooleanPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.CallbackPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.CameraPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.ColorPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.FloatPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.GraphLightsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.Vector2PipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.Vector3PipelinePropertyProducer;

public class RendererPipelineConfiguration {
    private static ObjectMap<String, PipelineNodeProducer> pipelineNodeProducers = new ObjectMap<>();
    private static Array<PipelinePropertyProducer> pipelinePropertyProducers = new Array<>();

    public static void register(PipelineNodeProducer pipelineNodeProducer) {
        pipelineNodeProducers.put(pipelineNodeProducer.getType(), pipelineNodeProducer);
    }

    static {
        register(new StartPipelineNodeProducer());
        register(new EndPipelineNodeProducer());
        register(new CustomRendererPipelineNodeProducer());
        register(new ModelShaderRendererPipelineNodeProducer());
        register(new SpriteShaderRendererPipelineNodeProducer());
        register(new ScreenShaderRendererPipelineNodeProducer());
        register(new PipelineRendererNodeProducer());

        register(new ValueFloatPipelineNodeProducer());
        register(new ValueVector2PipelineNodeProducer());
        register(new ValueVector3PipelineNodeProducer());
        register(new ValueColorPipelineNodeProducer());
        register(new ValueBooleanPipelineNodeProducer());

        register(new RenderSizePipelineNodeProducer());
        register(new TimePipelineNodeProducer());

        register(new AddPipelineNodeProducer());
        register(new SubtractPipelineNodeProducer());
        register(new MultiplyPipelineNodeProducer());
        register(new MergePipelineNodeProducer());
        register(new SplitPipelineNodeProducer());

        register(new PropertyPipelineNodeProducer());

        register(new BloomPipelineNodeProducer());
        register(new GaussianBlurPipelineNodeProducer());
        register(new DepthOfFieldPipelineNodeProducer());
        register(new GammaCorrectionPipelineNodeProducer());

        pipelinePropertyProducers.add(new FloatPipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector2PipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector3PipelinePropertyProducer());
        pipelinePropertyProducers.add(new ColorPipelinePropertyProducer());
        pipelinePropertyProducers.add(new BooleanPipelinePropertyProducer());
        pipelinePropertyProducers.add(new GraphLightsPipelinePropertyProducer());
        pipelinePropertyProducers.add(new CameraPipelinePropertyProducer());
        pipelinePropertyProducers.add(new CallbackPipelinePropertyProducer());
    }

    public static PipelineNodeProducer findProducer(String type) {
        return pipelineNodeProducers.get(type);
    }

    public static PipelinePropertyProducer findProperty(PipelineFieldType type) {
        for (PipelinePropertyProducer pipelinePropertyProducer : pipelinePropertyProducers) {
            if (pipelinePropertyProducer.getType() == type)
                return pipelinePropertyProducer;
        }
        return null;
    }

    private RendererPipelineConfiguration() {

    }
}
