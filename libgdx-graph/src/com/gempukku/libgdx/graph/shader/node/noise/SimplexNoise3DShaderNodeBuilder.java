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
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.math.value.RemapShaderNodeBuilder;

import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.dot;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.floor;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.fract;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.max;
import static com.gempukku.libgdx.graph.shader.node.noise.GLSLAdapter.step;

public class SimplexNoise3DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SimplexNoise3DShaderNodeBuilder() {
        super(new SimplexNoise3DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput pointValue = inputs.get("point");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput rangeValue = inputs.get("range");

        if (!commonShaderBuilder.containsFunction("simplexNoise3d")) {
            commonShaderBuilder.addFunction("simplexNoise3d", GLSLFragmentReader.getFragment("simplexNoise3d"));
        }

        commonShaderBuilder.addMainLine("// Simplex noise 3D node");
        String name = "result_" + nodeId;
        String output = "simplexNoise3d(" + pointValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ")";

        String noiseRange = "vec2(-3.1415, 3.1415)";
        if (rangeValue != null) {
            String functionName = RemapShaderNodeBuilder.appendRemapFunction(commonShaderBuilder, ShaderFieldType.Float);
            commonShaderBuilder.addMainLine("float " + name + " = " + functionName + "(" + output + ", " + noiseRange + ", " + rangeValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    private static Vector3 simplexNoise3d_random3(Vector3 c) {
        float j = 4096.0f * MathUtils.sin(dot(c, new Vector3(17.0f, 59.4f, 15.0f)));
        Vector3 r = new Vector3();
        r.z = fract(512.0f * j);
        j *= .125;
        r.x = fract(512.0f * j);
        j *= .125;
        r.y = fract(512.0f * j);
        return r.sub(0.5f);
    }

    /* skew constants for 3d simplex functions */
    private static final float simplexNoise3d_F3 = 0.3333333f;
    private static final float simplexNoise3d_G3 = 0.1666667f;

    /* 3d simplex noise */
    private static float simplexNoise3d(Vector3 p) {
        /* 1. find current tetrahedron T and it's four vertices */
        /* s, s+i1, s+i2, s+1.0 - absolute skewed (integer) coordinates of T vertices */
        /* x, x1, x2, x3 - unskewed coordinates of p relative to each of T vertices*/

        /* calculate s and x */
        Vector3 s = floor(new Vector3(p).add(dot(p, new Vector3(simplexNoise3d_F3, simplexNoise3d_F3, simplexNoise3d_F3))));
        Vector3 x = new Vector3(p).sub(s).add(dot(s, new Vector3(simplexNoise3d_G3, simplexNoise3d_G3, simplexNoise3d_G3)));

        /* calculate i1 and i2 */
        Vector3 e = step(new Vector3(), new Vector3(x).sub(x.y, x.z, x.x));
        Vector3 i1 = new Vector3(e.z, e.x, e.y).scl(-1).add(1f).scl(e);
        Vector3 i2 = new Vector3(e.z, e.x, e.y).scl(new Vector3(e).scl(-1f).add(1f)).scl(-1).add(1f);

        /* x1, x2, x3 */
        Vector3 x1 = new Vector3(x).sub(i1).add(simplexNoise3d_G3);
        Vector3 x2 = new Vector3(x).sub(i2).add(2f * simplexNoise3d_G3);
        Vector3 x3 = new Vector3(x).sub(1f).add(3f * simplexNoise3d_G3);

        /* 2. find four surflets and store them in d */
        Vector2 w1 = new Vector2();
        Vector2 w2 = new Vector2();
        Vector2 d1 = new Vector2();
        Vector2 d2 = new Vector2();

        /* calculate surflet weights */
        w1.x = dot(x, x);
        w1.y = dot(x1, x1);
        w2.x = dot(x2, x2);
        w2.y = dot(x3, x3);

        /* w fades from 0.6 at the center of the surflet to 0.0 at the margin */
        w1 = max(new Vector2(w1).scl(-1).add(0.6f, 0.6f), new Vector2());
        w2 = max(new Vector2(w2).scl(-1).add(0.6f, 0.6f), new Vector2());

        /* calculate surflet components */
        d1.x = dot(simplexNoise3d_random3(s), x);
        d1.y = dot(simplexNoise3d_random3(new Vector3(s).add(i1)), x1);
        d2.x = dot(simplexNoise3d_random3(new Vector3(s).add(i2)), x2);
        d2.y = dot(simplexNoise3d_random3(new Vector3(s).add(1.0f)), x3);

        /* multiply d by w^4 */
        w1 = w1.scl(w1);
        w1 = w1.scl(w1);
        w2 = w2.scl(w2);
        w2 = w2.scl(w2);
        d1 = d1.scl(w1);
        d2 = d2.scl(w2);

        /* 3. return the sum of the four surflets */
        return dot(d1, new Vector2(52f, 52f)) + dot(d2, new Vector2(52f, 52f));
    }

    public static void main(String[] args) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        // Calculate min and max of simplexNoise2d
        float v = 3f;
        for (float x = -v; x < v; x += 0.01f) {
            System.out.println("x: " + x);
            for (float y = -v; y < v; y += 0.01f) {
                for (float z = -v; z < v; z += 0.01f) {
                    float noise = simplexNoise3d(new Vector3(x, y, z));
                    min = Math.min(min, noise);
                    max = Math.max(max, noise);
                }
            }
        }
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }
}
