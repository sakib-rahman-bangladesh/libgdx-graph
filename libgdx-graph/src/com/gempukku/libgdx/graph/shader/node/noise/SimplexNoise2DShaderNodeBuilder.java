package com.gempukku.libgdx.graph.shader.node.noise;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.math.value.RemapShaderNodeBuilder;

import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.abs;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.dot;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.floor;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.fract;

public class SimplexNoise2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SimplexNoise2DShaderNodeBuilder() {
        super(new SimplexNoise2DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput rangeValue = inputs.get("range");

        if (!commonShaderBuilder.containsFunction("simplexNoise2d")) {
            commonShaderBuilder.addFunction("simplexNoise2d", GLSLFragmentReader.getFragment("simplexNoise2d"));
        }

        commonShaderBuilder.addMainLine("// Simplex noise 2D node");
        String name = "result_" + nodeId;
        String output;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            output = "simplexNoise2d(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ")";
        } else {
            output = "simplexNoise2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scaleValue.getRepresentation() + ")";
        }

        String noiseRange = "vec2(-1.0, 1.0)";
        if (rangeValue != null) {
            String functionName = RemapShaderNodeBuilder.appendRemapFunction(commonShaderBuilder, ShaderFieldType.Float);
            commonShaderBuilder.addMainLine("float " + name + " = " + functionName + "(" + output + ", " + noiseRange + ", " + rangeValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    private static Vector3 simplexNoise2d_mod289(Vector3 x) {
        return new Vector3(x).sub(
                MathUtils.floor(x.x * (1.0f / 289.0f)) * 289.0f,
                MathUtils.floor(x.y * (1.0f / 289.0f)) * 289.0f,
                MathUtils.floor(x.z * (1.0f / 289.0f)) * 289.0f);
    }

    private static Vector2 simplexNoise2d_mod289(Vector2 x) {
        return new Vector2(x).sub(MathUtils.floor(x.x * (1.0f / 289.0f)) * 289.0f,
                MathUtils.floor(x.y * (1.0f / 289.0f)) * 289.0f);
    }

    private static Vector3 simplexNoise2d_permute(Vector3 x) {
        return simplexNoise2d_mod289(new Vector3(x).scl(34.0f).add(1.0f).scl(x));
    }

    private static float simplexNoise2d(Vector2 v) {
        // Precompute values for skewed triangular grid
        float cx = 0.211324865405187f;
        float cy = 0.366025403784439f;
        float cz = -0.577350269189626f;
        float cw = 0.024390243902439f;

        // First corner (x0)
        Vector2 i = floor(new Vector2(v).add(dot(v, new Vector2(cy, cy)), dot(v, new Vector2(cy, cy))));
        Vector2 x0 = new Vector2(v).sub(i).add(dot(i, new Vector2(cx, cx)), dot(i, new Vector2(cx, cx)));

        // Other two corners (x1, x2)
        Vector2 i1 = (x0.x > x0.y) ? new Vector2(1.0f, 0.0f) : new Vector2(0.0f, 1.0f);
        Vector2 x1 = new Vector2(x0).add(cx, cx).sub(i1);
        Vector2 x2 = new Vector2(x0).add(cz, cz);

        // Do some permutations to avoid
        // truncation effects in permutation
        i = simplexNoise2d_mod289(i);
        Vector3 p = simplexNoise2d_permute(
                simplexNoise2d_permute(new Vector3(0.0f, i1.y, 1.0f).add(i.y)).add(i.x).add(new Vector3(0.0f, i1.x, 1.0f)));

        Vector3 m = new Vector3(
                Math.max(0.0f, 0.5f - x0.dot(x0)),
                Math.max(0.0f, 0.5f - x1.dot(x1)),
                Math.max(0.0f, 0.5f - x2.dot(x2)));

        m = m.scl(m);
        m = m.scl(m);

        // Gradients:
        //  41 pts uniformly over a line, mapped onto a diamond
        //  The ring size 17*17 = 289 is close to a multiple
        //      of 41 (41*7 = 287)

        Vector3 x = fract(new Vector3(p).scl(cw)).scl(2.0f).sub(1.0f);
        Vector3 h = abs(x).sub(0.5f);
        Vector3 ox = floor(new Vector3(x).add(0.5f));
        Vector3 a0 = new Vector3(x).sub(ox);

        // Normalise gradients implicitly by scaling m
        // Approximation of: m *= inversesqrt(a0*a0 + h*h);
        m.scl(new Vector3(a0).scl(a0).add(new Vector3(h).scl(h)).scl(-0.85373472095314f).add(1.79284291400159f));

        // Compute final noise value at P
        Vector3 g = new Vector3(0.0f, 0f, 0f);
        g.x = a0.x * x0.x + h.x * x0.y;
        g.y = a0.y * x1.x + h.y * x1.y;
        g.z = a0.z * x2.x + h.z * x2.y;
        return 130.0f * m.dot(g);
    }

    public static void main(String[] args) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        // Calculate min and max of simplexNoise2d
        for (float x = -10f; x < 10f; x += 0.01f) {
            for (float y = -10f; y < 10f; y += 0.01f) {
                float noise = simplexNoise2d(new Vector2(x, y));
                min = Math.min(min, noise);
                max = Math.max(max, noise);
            }
        }
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }
}
