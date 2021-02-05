package com.gempukku.libgdx.graph.ui.pipeline;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.AddPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.DividePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.OneMinusPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.ReciprocalPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.SubtractPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.AbsPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.CeilingPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.ClampPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.FloorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.FractionalPartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.LerpPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.MaximumPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.MinimumPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.ModuloPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SaturatePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SignPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.SmoothstepPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.common.StepPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.part.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.part.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.CustomRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueColorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector3PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PropertyPipelineGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor.DepthOfFieldBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyCallbackBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyCameraBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueVector3BoxProducer;

import java.util.Map;
import java.util.TreeMap;

public class UIPipelineConfiguration implements UIGraphConfiguration<PipelineFieldType> {
    private static Map<String, GraphBoxProducer<PipelineFieldType>> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer<PipelineFieldType>> propertyProducers = new TreeMap<>();

    public static void register(GraphBoxProducer<PipelineFieldType> producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        GraphBoxProducer<PipelineFieldType> endProducer = new GraphBoxProducerImpl<PipelineFieldType>(new EndPipelineNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        register(endProducer);

        register(new PropertyPipelineGraphBoxProducer());

        register(new ValueColorBoxProducer<PipelineFieldType>(new ValueColorPipelineNodeConfiguration()));
        register(new ValueFloatBoxProducer<PipelineFieldType>(new ValueFloatPipelineNodeConfiguration()));
        register(new ValueVector2BoxProducer<PipelineFieldType>(new ValueVector2PipelineNodeConfiguration()));
        register(new ValueVector3BoxProducer<PipelineFieldType>(new ValueVector3PipelineNodeConfiguration()));
        register(new ValueBooleanBoxProducer<PipelineFieldType>(new ValueBooleanPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new TimePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new RenderSizePipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new AddPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new DividePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new MultiplyPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new OneMinusPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new ReciprocalPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new SubtractPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new AbsPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new CeilingPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new ClampPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new FloorPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new FractionalPartPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new LerpPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new MaximumPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new MinimumPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new ModuloPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new SaturatePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new SignPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new SmoothstepPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new StepPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new SplitPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new MergePipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new StartPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new PipelineRendererNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new CustomRendererPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl<PipelineFieldType>(new BloomPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl<PipelineFieldType>(new GaussianBlurPipelineNodeConfiguration()));
        register(new DepthOfFieldBoxProducer());
        register(new GraphBoxProducerImpl<PipelineFieldType>(new GammaCorrectionPipelineNodeConfiguration()));

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        propertyProducers.put("Boolean", new PropertyBooleanBoxProducer());
        propertyProducers.put("Camera", new PropertyCameraBoxProducer());
        propertyProducers.put("Callback", new PropertyCallbackBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer<PipelineFieldType>> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer<PipelineFieldType>> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
