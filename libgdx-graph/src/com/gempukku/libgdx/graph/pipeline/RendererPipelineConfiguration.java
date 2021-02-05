package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.AddPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.DividePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.MultiplyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.OneMinusPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.ReciprocalPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.SubtractPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.AbsPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.CeilingPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.ClampPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.FloorPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.FractionalPartPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.LerpPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.MaximumPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.MinimumPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.ModuloPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.SaturatePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.SignPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.SmoothstepPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.StepPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.part.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.part.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.DepthOfFieldPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.GaussianBlurPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.provided.RenderSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.provided.TimePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.CustomRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PipelineRendererNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.ValueBooleanPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.ValueColorPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.ValueFloatPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.ValueVector2PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.ValueVector3PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.property.BooleanPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.CallbackPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.CameraPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.ColorPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.FloatPipelinePropertyProducer;
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
        register(new PipelineRendererNodeProducer());

        register(new ValueFloatPipelineNodeProducer());
        register(new ValueVector2PipelineNodeProducer());
        register(new ValueVector3PipelineNodeProducer());
        register(new ValueColorPipelineNodeProducer());
        register(new ValueBooleanPipelineNodeProducer());

        register(new RenderSizePipelineNodeProducer());
        register(new TimePipelineNodeProducer());

        register(new AddPipelineNodeProducer());
        register(new DividePipelineNodeProducer());
        register(new MultiplyPipelineNodeProducer());
        register(new OneMinusPipelineNodeProducer());
        register(new ReciprocalPipelineNodeProducer());
        register(new SubtractPipelineNodeProducer());

        register(new AbsPipelineNodeProducer());
        register(new CeilingPipelineNodeProducer());
        register(new ClampPipelineNodeProducer());
        register(new FloorPipelineNodeProducer());
        register(new FractionalPartPipelineNodeProducer());
        register(new LerpPipelineNodeProducer());
        register(new MaximumPipelineNodeProducer());
        register(new MinimumPipelineNodeProducer());
        register(new ModuloPipelineNodeProducer());
        register(new SaturatePipelineNodeProducer());
        register(new SignPipelineNodeProducer());
        register(new SmoothstepPipelineNodeProducer());
        register(new StepPipelineNodeProducer());

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
