package com.gempukku.libgdx.graph.shader.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.common.effect.FresnelEffectShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.effect.IntensityShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.AmbientLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.ApplyNormalMapShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.CalculateLightingShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.DirectionalLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.PointLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.lighting.SpotLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.AddShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.DivideShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.MultiplyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.OneMinusShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.ReciprocalShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.SubtractShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.AbsShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.CeilingShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.ClampShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.ConditionalShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.FloorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.FractionalPartShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.LerpShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.MaximumShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.MinimumShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.ModuloShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.SaturateShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.SignShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.SmoothstepShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.common.StepShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.ExponentialBase2ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.ExponentialShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.InverseSquareRootShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.LogarithmBase2ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.NaturalLogarithmShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.PowerShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.exponential.SquareRootShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.geometric.CrossProductShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.geometric.DistanceShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.geometric.DotProductShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.geometric.LengthShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.geometric.NormalizeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.ArccosShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.ArcsinShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.ArctanShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.CosShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.DegreesShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.RadiansShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.SinShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.TanShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.utility.DistanceFromPlaneShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.MergeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.RemapShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.SplitShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.PerlinNoise2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.PerlinNoise3DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.SimplexNoise2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.SimplexNoise3DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.VoronoiBorder2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.VoronoiBorder3DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.VoronoiDistance2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.noise.VoronoiDistance3DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.CameraDirectionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.CameraPositionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.FragmentCoordinateShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.PixelSizeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.SceneColorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.SceneDepthShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.ScreenPositionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.TimeGraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.provided.ViewportSizeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.shape.CheckerboardShapeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.shape.DotShapeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.shape.EllipseShapeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.shape.RectangleShapeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.shape.StarShapeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.Sampler2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVFlipbookShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVTilingAndOffsetShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.ValueBooleanShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.ValueColorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.ValueFloatShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.ValueVector2ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.ValueVector3ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ColorShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.FloatShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.TextureShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.Vector2ShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.Vector3ShaderPropertyProducer;

public class CommonShaderConfiguration implements GraphConfiguration {
    public static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    public static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();

    static {
        // Math - Arithmetic
        addGraphShaderNodeBuilder(new AddShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SubtractShaderNodeBuilder());
        addGraphShaderNodeBuilder(new OneMinusShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MultiplyShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DivideShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ReciprocalShaderNodeBuilder());

        // Math - exponential
        addGraphShaderNodeBuilder(new PowerShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ExponentialShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ExponentialBase2ShaderNodeBuilder());
        addGraphShaderNodeBuilder(new NaturalLogarithmShaderNodeBuilder());
        addGraphShaderNodeBuilder(new LogarithmBase2ShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SquareRootShaderNodeBuilder());
        addGraphShaderNodeBuilder(new InverseSquareRootShaderNodeBuilder());

        // Math - Common
        addGraphShaderNodeBuilder(new AbsShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SignShaderNodeBuilder());
        addGraphShaderNodeBuilder(new FloorShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CeilingShaderNodeBuilder());
        addGraphShaderNodeBuilder(new FractionalPartShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ModuloShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MinimumShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MaximumShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MaximumShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ClampShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SaturateShaderNodeBuilder());
        addGraphShaderNodeBuilder(new LerpShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ConditionalShaderNodeBuilder());
        addGraphShaderNodeBuilder(new StepShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SmoothstepShaderNodeBuilder());

        // Math - geometric
        addGraphShaderNodeBuilder(new LengthShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DistanceShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DotProductShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CrossProductShaderNodeBuilder());
        addGraphShaderNodeBuilder(new NormalizeShaderNodeBuilder());

        // Math - advanced
        addGraphShaderNodeBuilder(new MergeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SplitShaderNodeBuilder());
        addGraphShaderNodeBuilder(new RemapShaderNodeBuilder());

        // Math - trigonometry
        addGraphShaderNodeBuilder(new SinShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CosShaderNodeBuilder());
        addGraphShaderNodeBuilder(new TanShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ArcsinShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ArccosShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ArctanShaderNodeBuilder());
        addGraphShaderNodeBuilder(new RadiansShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DegreesShaderNodeBuilder());

        // Math - utilities
        addGraphShaderNodeBuilder(new DistanceFromPlaneShaderNodeBuilder());

        // Lighting
        addGraphShaderNodeBuilder(new CalculateLightingShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ApplyNormalMapShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AmbientLightShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DirectionalLightShaderNodeBuilder());
        addGraphShaderNodeBuilder(new PointLightShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SpotLightShaderNodeBuilder());

        // Effect
        addGraphShaderNodeBuilder(new FresnelEffectShaderNodeBuilder());
        addGraphShaderNodeBuilder(new IntensityShaderNodeBuilder());

        // Texture
        addGraphShaderNodeBuilder(new Sampler2DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new UVFlipbookShaderNodeBuilder());
        addGraphShaderNodeBuilder(new UVTilingAndOffsetShaderNodeBuilder());

        // Noise
        addGraphShaderNodeBuilder(new SimplexNoise2DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SimplexNoise3DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new PerlinNoise2DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new PerlinNoise3DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new VoronoiDistance2DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new VoronoiDistance3DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new VoronoiBorder2DShaderNodeBuilder());
        addGraphShaderNodeBuilder(new VoronoiBorder3DShaderNodeBuilder());

        // Shape
        addGraphShaderNodeBuilder(new DotShapeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CheckerboardShapeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new EllipseShapeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new RectangleShapeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new StarShapeShaderNodeBuilder());

        // Provided
        addGraphShaderNodeBuilder(new TimeGraphShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CameraPositionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new CameraDirectionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new FragmentCoordinateShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SceneDepthShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SceneColorShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ScreenPositionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new PixelSizeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ViewportSizeShaderNodeBuilder());

        // Values
        addGraphShaderNodeBuilder(new ValueBooleanShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueColorShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueFloatShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector2ShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector3ShaderNodeBuilder());

        graphShaderPropertyProducers.add(new ColorShaderPropertyProducer());
        graphShaderPropertyProducers.add(new FloatShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector2ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector3ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new TextureShaderPropertyProducer());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    @Override
    public Array<GraphShaderPropertyProducer> getPropertyProducers() {
        return graphShaderPropertyProducers;
    }

    @Override
    public GraphShaderNodeBuilder getGraphShaderNodeBuilder(String type) {
        return graphShaderNodeBuilders.get(type);
    }
}
