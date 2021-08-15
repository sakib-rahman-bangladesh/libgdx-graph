package com.gempukku.libgdx.graph.ui.shader;

import com.gempukku.libgdx.graph.shader.common.sprite.BillboardSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.common.sprite.ScreenSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.IntensityShaderNodeConfiguration;
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
import com.gempukku.libgdx.graph.ui.shader.producer.effect.GradientShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.common.ConditionalShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapValueShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapVectorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyMatrix4BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyShaderGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyTextureBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.SceneColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.TimeShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.texture.UVFlipbookShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.ValueVector3BoxProducer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UICommonShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyBoxProducer propertyBoxProducer) {
        propertyProducers.put(propertyBoxProducer.getType(), propertyBoxProducer);
    }

    static {
        register(new PropertyShaderGraphBoxProducer());

        register(new GraphBoxProducerImpl(new Sampler2DShaderNodeConfiguration()));
        register(new UVFlipbookShaderBoxProducer());
        register(new GraphBoxProducerImpl(new UVTilingAndOffsetShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new BorderDetectionShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AddShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SubtractShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new OneMinusShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MultiplyShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DivideShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ReciprocalShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new PowerShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ExponentialShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ExponentialBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NaturalLogarithmShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LogarithmBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SquareRootShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new InverseSquareRootShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new SinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new TanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArcsinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArccosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArctanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RadiansShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DegreesShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AbsShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SignShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FloorShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CeilingShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FractionalPartShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ModuloShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MinimumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MaximumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ClampShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SaturateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LerpShaderNodeConfiguration()));
        register(new ConditionalShaderBoxProducer());
        register(new GraphBoxProducerImpl(new StepShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SmoothstepShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new LengthShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DistanceShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DotProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CrossProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NormalizeShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new DistanceFromPlaneShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new SplitShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MergeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RemapShaderNodeConfiguration()));
        register(new RemapVectorShaderBoxProducer());
        register(new RemapValueShaderBoxProducer());

        register(new GraphBoxProducerImpl(new IntensityShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FresnelEffectShaderNodeConfiguration()));
        register(new GradientShaderBoxProducer());

        register(new GraphBoxProducerImpl(new SimplexNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SimplexNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PerlinNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PerlinNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiDistance2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiDistance3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiBorder2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiBorder3DNodeConfiguration()));

        register(new GraphBoxProducerImpl(new DotShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CheckerboardShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new EllipseShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RectangleShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new StarShapeShaderNodeConfiguration()));

        register(new TimeShaderBoxProducer());
        register(new GraphBoxProducerImpl(new CameraPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CameraDirectionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SceneDepthShaderNodeConfiguration()));
        register(new SceneColorShaderBoxProducer());
        register(new GraphBoxProducerImpl(new ScreenPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PixelSizeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ViewportSizeShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new BillboardSpriteShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ScreenSpriteShaderNodeConfiguration()));

        register(new ValueColorBoxProducer(new ValueColorShaderNodeConfiguration()));
        register(new ValueFloatBoxProducer(new ValueFloatShaderNodeConfiguration()));
        register(new ValueVector2BoxProducer(new ValueVector2ShaderNodeConfiguration()));
        register(new ValueVector3BoxProducer(new ValueVector3ShaderNodeConfiguration()));
        register(new ValueBooleanBoxProducer(new ValueBooleanShaderNodeConfiguration()));

        registerPropertyType(new PropertyFloatBoxProducer());
        registerPropertyType(new PropertyVector2BoxProducer());
        registerPropertyType(new PropertyVector3BoxProducer());
        registerPropertyType(new PropertyColorBoxProducer());
        registerPropertyType(new PropertyMatrix4BoxProducer());
        registerPropertyType(new PropertyTextureBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
