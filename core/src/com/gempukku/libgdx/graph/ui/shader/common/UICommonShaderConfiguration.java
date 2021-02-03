package com.gempukku.libgdx.graph.ui.shader.common;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.common.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.AmbientLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.ApplyNormalMapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.CalculateLightingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.DirectionalLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.PointLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.lighting.SpotLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.AddShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.DivideShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.MultiplyShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.OneMinusShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.ReciprocalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.SubtractShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.AbsShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.CeilingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ClampShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.FloorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.FractionalPartShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.LerpShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.MaximumShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.MinimumShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ModuloShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.SaturateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.SignShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.SmoothstepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.common.StepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.ExponentialBase2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.ExponentialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.InverseSquareRootShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.LogarithmBase2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.NaturalLogarithmShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.PowerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.SquareRootShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.CrossProductShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.DistanceShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.DotProductShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.LengthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.NormalizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.ArccosShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.ArcsinShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.ArctanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.CosShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.DegreesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.RadiansShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.SinShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.TanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.utility.DistanceFromPlaneShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.PerlinNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.PerlinNoise3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.SimplexNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.SimplexNoise3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiBorder2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiBorder3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiDistance2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiDistance3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.CameraDirectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.CameraPositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.FragmentCoordinateShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.PixelSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneDepthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.ScreenPositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.provided.ViewportSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.shape.CheckerboardShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.shape.DotShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.shape.EllipseShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.shape.RectangleShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.shape.StarShapeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.BorderDetectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVTilingAndOffsetShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueBooleanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueVector2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.ValueVector3ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.IndexedBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.effect.GradientShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.math.common.ConditionalShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.math.value.RemapValueShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.math.value.RemapVectorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyShaderGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyTextureBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.provided.SceneColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.provided.TimeShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.texture.UVFlipbookShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.common.producer.value.ValueVector3BoxProducer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UICommonShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    private static Map<String, GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer<ShaderFieldType>> propertyProducers = new LinkedHashMap<>();

    public static void register(GraphBoxProducer<ShaderFieldType> producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new PropertyShaderGraphBoxProducer());

        register(new GraphBoxProducerImpl<ShaderFieldType>(new CalculateLightingShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ApplyNormalMapShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new AmbientLightShaderNodeConfiguration()));
        register(new IndexedBoxProducer<ShaderFieldType>(new DirectionalLightShaderNodeConfiguration()));
        register(new IndexedBoxProducer<ShaderFieldType>(new PointLightShaderNodeConfiguration()));
        register(new IndexedBoxProducer<ShaderFieldType>(new SpotLightShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new Sampler2DShaderNodeConfiguration()));
        register(new UVFlipbookShaderBoxProducer());
        register(new GraphBoxProducerImpl<ShaderFieldType>(new UVTilingAndOffsetShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new BorderDetectionShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new AddShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SubtractShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new OneMinusShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new MultiplyShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new DivideShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ReciprocalShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new PowerShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ExponentialShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ExponentialBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new NaturalLogarithmShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new LogarithmBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SquareRootShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new InverseSquareRootShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new SinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new TanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ArcsinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ArccosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ArctanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new RadiansShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new DegreesShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new AbsShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SignShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new FloorShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CeilingShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new FractionalPartShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ModuloShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new MinimumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new MaximumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ClampShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SaturateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new LerpShaderNodeConfiguration()));
        register(new ConditionalShaderBoxProducer());
        register(new GraphBoxProducerImpl<ShaderFieldType>(new StepShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SmoothstepShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new LengthShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new DistanceShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new DotProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CrossProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new NormalizeShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new DistanceFromPlaneShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new SplitShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new MergeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new RemapShaderNodeConfiguration()));
        register(new RemapVectorShaderBoxProducer());
        register(new RemapValueShaderBoxProducer());

        register(new GraphBoxProducerImpl<ShaderFieldType>(new IntensityShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new FresnelEffectShaderNodeConfiguration()));
        register(new GradientShaderBoxProducer());

        register(new GraphBoxProducerImpl<ShaderFieldType>(new SimplexNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SimplexNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new PerlinNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new PerlinNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiDistance2DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiDistance3DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiBorder2DNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiBorder3DNodeConfiguration()));

        register(new GraphBoxProducerImpl<ShaderFieldType>(new DotShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CheckerboardShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new EllipseShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new RectangleShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new StarShapeShaderNodeConfiguration()));

        register(new TimeShaderBoxProducer());
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CameraPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new CameraDirectionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new FragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SceneDepthShaderNodeConfiguration()));
        register(new SceneColorShaderBoxProducer());
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ScreenPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new PixelSizeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ViewportSizeShaderNodeConfiguration()));

        register(new ValueColorBoxProducer<ShaderFieldType>(new ValueColorShaderNodeConfiguration()));
        register(new ValueFloatBoxProducer<ShaderFieldType>(new ValueFloatShaderNodeConfiguration()));
        register(new ValueVector2BoxProducer<ShaderFieldType>(new ValueVector2ShaderNodeConfiguration()));
        register(new ValueVector3BoxProducer<ShaderFieldType>(new ValueVector3ShaderNodeConfiguration()));
        register(new ValueBooleanBoxProducer<ShaderFieldType>(new ValueBooleanShaderNodeConfiguration()));

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        propertyProducers.put("Texture", new PropertyTextureBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
