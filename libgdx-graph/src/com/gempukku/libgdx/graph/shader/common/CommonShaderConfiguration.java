package com.gempukku.libgdx.graph.shader.common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.common.effect.FresnelEffectShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.effect.GradientShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.effect.IntensityShaderNodeBuilder;
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
import com.gempukku.libgdx.graph.shader.common.math.value.RemapValueShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.RemapVectorShaderNodeBuilder;
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
import com.gempukku.libgdx.graph.shader.common.texture.BorderDetectionShaderNodeBuilder;
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
    private static ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    private static Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();
    private static TextureRegion defaultTextureRegion;


    public static void register(GraphShaderNodeBuilder graphShaderNodeBuilder) {
        graphShaderNodeBuilders.put(graphShaderNodeBuilder.getType(), graphShaderNodeBuilder);
    }

    public static void setDefaultTextureRegionProperty(TextureRegion textureRegion) {
        defaultTextureRegion = textureRegion;
    }

    static {
        // Math - Arithmetic
        register(new AddShaderNodeBuilder());
        register(new SubtractShaderNodeBuilder());
        register(new OneMinusShaderNodeBuilder());
        register(new MultiplyShaderNodeBuilder());
        register(new DivideShaderNodeBuilder());
        register(new ReciprocalShaderNodeBuilder());

        // Math - exponential
        register(new PowerShaderNodeBuilder());
        register(new ExponentialShaderNodeBuilder());
        register(new ExponentialBase2ShaderNodeBuilder());
        register(new NaturalLogarithmShaderNodeBuilder());
        register(new LogarithmBase2ShaderNodeBuilder());
        register(new SquareRootShaderNodeBuilder());
        register(new InverseSquareRootShaderNodeBuilder());

        // Math - Common
        register(new AbsShaderNodeBuilder());
        register(new SignShaderNodeBuilder());
        register(new FloorShaderNodeBuilder());
        register(new CeilingShaderNodeBuilder());
        register(new FractionalPartShaderNodeBuilder());
        register(new ModuloShaderNodeBuilder());
        register(new MinimumShaderNodeBuilder());
        register(new MaximumShaderNodeBuilder());
        register(new MaximumShaderNodeBuilder());
        register(new ClampShaderNodeBuilder());
        register(new SaturateShaderNodeBuilder());
        register(new LerpShaderNodeBuilder());
        register(new ConditionalShaderNodeBuilder());
        register(new StepShaderNodeBuilder());
        register(new SmoothstepShaderNodeBuilder());

        // Math - geometric
        register(new LengthShaderNodeBuilder());
        register(new DistanceShaderNodeBuilder());
        register(new DotProductShaderNodeBuilder());
        register(new CrossProductShaderNodeBuilder());
        register(new NormalizeShaderNodeBuilder());

        // Math - advanced
        register(new MergeShaderNodeBuilder());
        register(new SplitShaderNodeBuilder());
        register(new RemapShaderNodeBuilder());
        register(new RemapVectorShaderNodeBuilder());
        register(new RemapValueShaderNodeBuilder());

        // Math - trigonometry
        register(new SinShaderNodeBuilder());
        register(new CosShaderNodeBuilder());
        register(new TanShaderNodeBuilder());
        register(new ArcsinShaderNodeBuilder());
        register(new ArccosShaderNodeBuilder());
        register(new ArctanShaderNodeBuilder());
        register(new RadiansShaderNodeBuilder());
        register(new DegreesShaderNodeBuilder());

        // Math - utilities
        register(new DistanceFromPlaneShaderNodeBuilder());

        // Effect
        register(new FresnelEffectShaderNodeBuilder());
        register(new IntensityShaderNodeBuilder());
        register(new GradientShaderNodeBuilder());

        // Texture
        register(new Sampler2DShaderNodeBuilder());
        register(new UVFlipbookShaderNodeBuilder());
        register(new UVTilingAndOffsetShaderNodeBuilder());
        register(new BorderDetectionShaderNodeBuilder());

        // Noise
        register(new SimplexNoise2DShaderNodeBuilder());
        register(new SimplexNoise3DShaderNodeBuilder());
        register(new PerlinNoise2DShaderNodeBuilder());
        register(new PerlinNoise3DShaderNodeBuilder());
        register(new VoronoiDistance2DShaderNodeBuilder());
        register(new VoronoiDistance3DShaderNodeBuilder());
        register(new VoronoiBorder2DShaderNodeBuilder());
        register(new VoronoiBorder3DShaderNodeBuilder());

        // Shape
        register(new DotShapeShaderNodeBuilder());
        register(new CheckerboardShapeShaderNodeBuilder());
        register(new EllipseShapeShaderNodeBuilder());
        register(new RectangleShapeShaderNodeBuilder());
        register(new StarShapeShaderNodeBuilder());

        // Provided
        register(new TimeGraphShaderNodeBuilder());
        register(new CameraPositionShaderNodeBuilder());
        register(new CameraDirectionShaderNodeBuilder());
        register(new FragmentCoordinateShaderNodeBuilder());
        register(new SceneDepthShaderNodeBuilder());
        register(new SceneColorShaderNodeBuilder());
        register(new ScreenPositionShaderNodeBuilder());
        register(new PixelSizeShaderNodeBuilder());
        register(new ViewportSizeShaderNodeBuilder());

        // Values
        register(new ValueBooleanShaderNodeBuilder());
        register(new ValueColorShaderNodeBuilder());
        register(new ValueFloatShaderNodeBuilder());
        register(new ValueVector2ShaderNodeBuilder());
        register(new ValueVector3ShaderNodeBuilder());

        graphShaderPropertyProducers.add(new ColorShaderPropertyProducer());
        graphShaderPropertyProducers.add(new FloatShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector2ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector3ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new TextureShaderPropertyProducer() {
            @Override
            protected TextureRegion getDefaultTextureRegion() {
                return defaultTextureRegion;
            }
        });
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
