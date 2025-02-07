package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.*;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.screen.ScreenGraphShader;
import com.gempukku.libgdx.graph.plugin.screen.ScreenShaderConfiguration;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteShaderConfiguration;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public class GraphShaderBuilder {
    private static GraphConfiguration[] modelConfigurations = new GraphConfiguration[]{
            new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ModelShaderConfiguration()};
    private static GraphConfiguration[] screenConfigurations = new GraphConfiguration[]{
            new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ScreenShaderConfiguration()};
    private static GraphConfiguration[] particleConfigurations = new GraphConfiguration[]{
            new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new ParticlesShaderConfiguration()};
    private static GraphConfiguration[] spriteConfigurations = new GraphConfiguration[]{
            new CommonShaderConfiguration(), new PropertyShaderConfiguration(), new SpriteShaderConfiguration()};

    public static SpriteGraphShader buildSpriteShader(String tag, Texture defaultTexture,
                                                      Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                      boolean designTime) {

        SpriteGraphShader graphShader = new SpriteGraphShader(tag, defaultTexture);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);

        buildSpriteVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildSpriteFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("sprite", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    public static ParticlesGraphShader buildParticlesShader(String tag, Texture defaultTexture, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                            boolean designTime) {
        ParticlesGraphShader graphShader = new ParticlesGraphShader(tag, defaultTexture);

        GraphNode endNode = graph.getNodeById("end");
        JsonValue data = endNode.getData();
        int maxNumberOfParticles = data.getInt("maxParticles", 100);

        graphShader.setMaxNumberOfParticles(maxNumberOfParticles);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);

        buildParticlesVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildParticlesFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("particles", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    public static ScreenGraphShader buildScreenShader(String tag, Texture defaultTexture,
                                                      Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                      boolean designTime) {
        ScreenGraphShader graphShader = new ScreenGraphShader(tag, defaultTexture);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, screenConfigurations);

        buildScreenVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildScreenFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("screen", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    public static ModelGraphShader buildModelShader(String tag, Texture defaultTexture,
                                                    Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                    boolean designTime) {
        ModelGraphShader graphShader = new ModelGraphShader(tag, defaultTexture);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);

        buildModelVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildModelFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("color", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    public static ModelGraphShader buildModelDepthShader(String tag, Texture defaultTexture,
                                                         Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                         boolean designTime) {
        ModelGraphShader graphShader = new ModelGraphShader(tag, defaultTexture);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);

        buildModelVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildDepthFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("depth", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    private static void initialize(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
                                   GraphConfiguration... graphConfigurations) {
        initializePropertyMap(graphShader, graph, designTime, graphConfigurations);
        initializeShaders(vertexShaderBuilder, fragmentShaderBuilder);
        setupOpenGLSettings(graphShader, graph);
    }

    private static void finalizeShader(GraphShader graphShader, String vertexShader, String fragmentShader) {
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        graphShader.setProgram(shaderProgram);
        graphShader.init();
    }

    private static void initializePropertyMap(GraphShader graphShader, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime,
                                              GraphConfiguration... graphConfigurations) {
        int index = 0;
        for (GraphProperty property : graph.getProperties()) {
            String name = property.getName();
            graphShader.addPropertySource(name, findPropertyProducerByType(property.getType(), graphConfigurations).createProperty(index++, name, property.getData(), property.getLocation(), designTime));
        }
    }

    private static void setupOpenGLSettings(GraphShader graphShader, Graph graph) {
        GraphNode endNode = graph.getNodeById("end");
        JsonValue data = endNode.getData();

        String cullingValue = data.getString("culling", null);
        if (cullingValue != null)
            graphShader.setCulling(BasicShader.Culling.valueOf(cullingValue));

        String blending = data.getString("blending", null);
        if (blending != null)
            graphShader.setBlending(BasicShader.Blending.valueOf(blending));

        String depthTest = data.getString("depthTest", null);
        if (depthTest != null)
            graphShader.setDepthTesting(BasicShader.DepthTesting.valueOf(depthTest.replace(' ', '_')));
    }

    private static void debugShaders(String type, String vertexShader, String fragmentShader) {
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "Vertex " + type + " shader:");
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "\n" + vertexShader);
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "Fragment " + type + " shader:");
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "\n" + fragmentShader);
    }

    private static void buildDepthFragmentShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
                                                 GraphConfiguration... graphConfigurations) {
        fragmentShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping);
        vertexShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);
        fragmentShaderBuilder.addFunction("packFloatToVec3", GLSLFragmentReader.getFragment("packFloatToVec3"));

        if (!vertexShaderBuilder.hasVaryingVariable("v_camera_distance")) {
            fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");

            vertexShaderBuilder.addMainLine("// Camera distance");
            vertexShaderBuilder.addVaryingVariable("v_camera_distance", "float");
            vertexShaderBuilder.addMainLine("v_camera_distance = distance(v_position_world, u_cameraPosition);");

            fragmentShaderBuilder.addVaryingVariable("v_camera_distance", "float");
        }

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);
        fragmentShaderBuilder.addMainLine("gl_FragColor = vec4(packFloatToVec3(v_camera_distance, u_cameraClipping.x, u_cameraClipping.y), 1.0);");
    }

    private static void buildSpriteFragmentShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertices(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);

        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildParticlesFragmentShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Fragment part
        if (!vertexShaderBuilder.hasVaryingVariable("v_deathTime")) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(2048, 1, "a_deathTime"), "float");
            vertexShaderBuilder.addVaryingVariable("v_deathTime", "float");
            vertexShaderBuilder.addMainLine("v_deathTime = a_deathTime;");

            fragmentShaderBuilder.addVaryingVariable("v_deathTime", "float");
        }

        fragmentShaderBuilder.addUniformVariable("u_time", "float", true, UniformSetters.time);
        fragmentShaderBuilder.addMainLine("if (u_time >= v_deathTime)");
        fragmentShaderBuilder.addMainLine("  discard;");

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertices(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);


        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildScreenFragmentShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Fragment part
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, screenConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, screenConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertices(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, screenConfigurations);
        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildModelFragmentShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Fragment part
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertices(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);
        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildModelVertexShader(
            Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
            boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);

        String positionType = graph.getNodeById("end").getData().getString("positionType", "World space");
        if (positionType.equals("World space")) {
            vertexShaderBuilder.addMainLine("vec3 positionWorld = " + positionField.getRepresentation() + ";");
        } else if (positionType.equals("Object space")) {
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans);
            vertexShaderBuilder.addMainLine("vec3 positionWorld = (u_worldTrans * vec4(" + positionField.getRepresentation() + ", 1.0)).xyz;");
        }

        vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
        vertexShaderBuilder.addMainLine("v_position_world = positionWorld;");

        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * vec4(positionWorld, 1.0);");
    }

    private static void applyAlphaDiscard(FragmentShaderBuilder fragmentShaderBuilder, GraphShaderNodeBuilder.FieldOutput alphaField, String alpha, GraphShaderNodeBuilder.FieldOutput alphaClipField, String alphaClip) {
        if (alphaField != null || alphaClipField != null) {
            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }

    private static void buildSpriteVertexShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        vertexShaderBuilder.addMainLine("// End Graph Node");

        String positionType = graph.getNodeById("end").getData().getString("positionType", "World space");
        if (positionType.equals("World space")) {
            vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
            vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * vec4(" + positionField.getRepresentation() + ", 1.0);");
        } else if (positionType.equals("Screen space")) {
            vertexShaderBuilder.addUniformVariable("u_viewportSize", "vec2", true, UniformSetters.viewportSize);
            vertexShaderBuilder.addMainLine("gl_Position = vec4((2.0 * " + positionField.getRepresentation() + " / vec3(u_viewportSize, 1.0)) - vec3(1.0, 1.0, 0.0), 1.0);");
        }
    }

    private static void buildParticlesVertexShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);

        // Vertex part
        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * vec4(" + positionField.getRepresentation() + ", 1.0);");
    }

    private static void buildScreenVertexShader(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), "vec3");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = vec4((a_position.xy * 2.0 - 1.0), 1.0, 1.0);");
    }

    private static GraphShaderNodeBuilder.FieldOutput getOutput(Array<GraphConnection> connections,
                                                                boolean designTime, boolean fragmentShader,
                                                                Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                                GraphShaderContext context, GraphShader graphShader, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
                                                                VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration... graphConfigurations) {
        if (connections == null || connections.size == 0)
            return null;
        GraphConnection connection = connections.get(0);
        ObjectMap<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, connection.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        return output.get(connection.getFieldFrom());
    }


    private static void initializeShaders(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        vertexShaderBuilder.addInitialLine("#ifdef GL_ES");
        vertexShaderBuilder.addInitialLine("#define LOWP lowp");
        vertexShaderBuilder.addInitialLine("#define MED mediump");
        vertexShaderBuilder.addInitialLine("#define HIGH highp");
        vertexShaderBuilder.addInitialLine("precision mediump float;");
        vertexShaderBuilder.addInitialLine("#else");
        vertexShaderBuilder.addInitialLine("#define MED");
        vertexShaderBuilder.addInitialLine("#define LOWP");
        vertexShaderBuilder.addInitialLine("#define HIGH");
        vertexShaderBuilder.addInitialLine("#endif");

        fragmentShaderBuilder.addInitialLine("#ifdef GL_ES");
        fragmentShaderBuilder.addInitialLine("#define LOWP lowp");
        fragmentShaderBuilder.addInitialLine("#define MED mediump");
        fragmentShaderBuilder.addInitialLine("#define HIGH highp");
        fragmentShaderBuilder.addInitialLine("precision mediump float;");
        fragmentShaderBuilder.addInitialLine("#else");
        fragmentShaderBuilder.addInitialLine("#define MED");
        fragmentShaderBuilder.addInitialLine("#define LOWP");
        fragmentShaderBuilder.addInitialLine("#define HIGH");
        fragmentShaderBuilder.addInitialLine("#endif");
    }

    private static ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> buildNode(
            boolean designTime, boolean fragmentShader,
            Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
            GraphShaderContext context, GraphShader graphShader, String nodeId, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration... graphConfigurations) {
        ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> nodeOutput = nodeOutputs.get(nodeId);
        if (nodeOutput == null) {
            GraphNode nodeInfo = graph.getNodeById(nodeId);
            String nodeInfoType = nodeInfo.getConfiguration().getType();
            GraphShaderNodeBuilder nodeBuilder = getNodeBuilder(nodeInfoType, graphConfigurations);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfoType);
            ObjectMap<String, Array<GraphShaderNodeBuilder.FieldOutput>> inputFields = new ObjectMap<>();
            for (GraphNodeInput nodeInput : new ObjectMap.Values<>(nodeBuilder.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
                String fieldId = nodeInput.getFieldId();
                Array<GraphConnection> vertexInfos = findInputVertices(graph, nodeId, fieldId);
                if (vertexInfos.size == 0 && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                Array<String> fieldTypes = new Array<>();
                Array<GraphShaderNodeBuilder.FieldOutput> fieldOutputs = new Array<>();
                for (GraphConnection vertexInfo : vertexInfos) {
                    ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    fieldTypes.add(fieldType.getName());
                    fieldOutputs.add(fieldOutput);
                }
                if (!nodeInput.acceptsInputTypes(fieldTypes))
                    throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                inputFields.put(fieldId, fieldOutputs);
            }
            ObjectSet<String> requiredOutputs = findRequiredOutputs(graph, nodeId);
            if (fragmentShader) {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildFragmentNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context, graphShader);
            } else {
                nodeOutput = (ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildVertexNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, context, graphShader);
            }
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private static GraphShaderNodeBuilder getNodeBuilder(String nodeInfoType, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            GraphShaderNodeBuilder graphShaderNodeBuilder = configuration.getGraphShaderNodeBuilder(nodeInfoType);
            if (graphShaderNodeBuilder != null)
                return graphShaderNodeBuilder;
        }

        return null;
    }

    private static ObjectSet<String> findRequiredOutputs(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                         String nodeId) {
        ObjectSet<String> result = new ObjectSet<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId))
                result.add(vertex.getFieldFrom());
        }
        return result;
    }

    private static Array<GraphConnection> findInputVertices(Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph,
                                                            String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }

    private static GraphShaderPropertyProducer findPropertyProducerByType(String type, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            for (GraphShaderPropertyProducer graphShaderPropertyProducer : configuration.getPropertyProducers()) {
                if (graphShaderPropertyProducer.getType().getName().equals(type))
                    return graphShaderPropertyProducer;
            }
        }

        return null;
    }
}
