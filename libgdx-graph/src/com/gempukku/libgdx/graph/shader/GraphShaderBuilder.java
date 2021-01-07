package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsAttributeShaderConfiguration;
import com.gempukku.libgdx.graph.shader.common.PropertyAsUniformShaderConfiguration;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.model.ModelGraphShader;
import com.gempukku.libgdx.graph.shader.model.ModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.shader.particles.ParticlesShaderConfiguration;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.shader.screen.ScreenGraphShader;
import com.gempukku.libgdx.graph.shader.screen.ScreenShaderConfiguration;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteShaderConfiguration;

public class GraphShaderBuilder {
    private static GraphConfiguration[] modelConfigurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ModelShaderConfiguration()};
    private static GraphConfiguration[] screenConfigurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ScreenShaderConfiguration()};
    private static GraphConfiguration[] particleConfigurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsUniformShaderConfiguration(), new ParticlesShaderConfiguration()};
    private static GraphConfiguration[] spriteConfigurations = new GraphConfiguration[]{new CommonShaderConfiguration(), new PropertyAsAttributeShaderConfiguration(), new SpriteShaderConfiguration()};

    public static SpriteGraphShader buildSpriteShader(Texture defaultTexture, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                      boolean designTime) {

        SpriteGraphShader graphShader = new SpriteGraphShader(defaultTexture);

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initialize(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);

        buildSpriteVertexShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);
        buildSpriteFragmentShader(graph, designTime, graphShader, vertexShaderBuilder, fragmentShaderBuilder);

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        graphShader.setVertexAttributes(vertexShaderBuilder.getVertexAttributes());

        debugShaders("sprite", vertexShader, fragmentShader);

        finalizeShader(graphShader, vertexShader, fragmentShader);

        return graphShader;
    }

    public static ParticlesGraphShader buildParticlesShader(Texture defaultTexture, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                            boolean designTime) {

        ParticlesGraphShader graphShader = new ParticlesGraphShader(defaultTexture);

        GraphNode<ShaderFieldType> endNode = graph.getNodeById("end");
        JsonValue data = endNode.getData();
        int maxNumberOfParticles = data.getInt("maxParticles", 100);
        int initialParticles = data.getInt("initialParticles", 0);
        float perSecondParticles = data.getFloat("perSecondParticles", 1f);

        graphShader.setMaxNumberOfParticles(maxNumberOfParticles);
        graphShader.setInitialParticles(initialParticles);
        graphShader.setPerSecondParticles(perSecondParticles);

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

    public static ScreenGraphShader buildScreenShader(Texture defaultTexture,
                                                      Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                      boolean designTime) {
        ScreenGraphShader graphShader = new ScreenGraphShader(defaultTexture);

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

    public static ModelGraphShader buildModelShader(Texture defaultTexture,
                                                    Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                    boolean designTime) {
        ModelGraphShader graphShader = new ModelGraphShader(defaultTexture);

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

    public static ModelGraphShader buildModelDepthShader(Texture defaultTexture,
                                                         Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                         boolean designTime) {
        ModelGraphShader graphShader = new ModelGraphShader(defaultTexture);

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

    private static void initialize(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
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

    private static void initializePropertyMap(GraphShader graphShader, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime,
                                              GraphConfiguration... graphConfigurations) {
        ObjectMap<String, PropertySource> propertyMap = new ObjectMap<>();
        int index = 0;
        for (GraphProperty<ShaderFieldType> property : graph.getProperties()) {
            String name = property.getName();
            propertyMap.put(name, findPropertyProducerByType(property.getType(), graphConfigurations).createProperty(index++, name, property.getData(), designTime));
        }
        graphShader.setPropertySourceMap(propertyMap);
    }

    private static void setupOpenGLSettings(GraphShader graphShader, Graph graph) {
        GraphNode<ShaderFieldType> endNode = graph.getNodeById("end");
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

    private static void buildDepthFragmentShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
                                                 GraphConfiguration... graphConfigurations) {
        fragmentShaderBuilder.addUniformVariable("u_cameraClipping", "vec2", true, UniformSetters.cameraClipping);
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);
        fragmentShaderBuilder.addFunction("packFloatToVec3", GLSLFragmentReader.getFragment("packFloatToVec3"));

        // Get position
        if (!vertexShaderBuilder.hasVaryingVariable("v_position_world")) {
            vertexShaderBuilder.addMainLine("// Attribute Position Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
            vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
            vertexShaderBuilder.addMainLine("v_position_world = (u_worldTrans * skinning * vec4(a_position, 1.0)).xyz;");

            fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");
        }

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertices(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertices(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        applyAlphaDiscard(fragmentShaderBuilder, alphaField, alpha, alphaClipField, alphaClip);
        fragmentShaderBuilder.addMainLine("gl_FragColor = vec4(packFloatToVec3(distance(v_position_world, u_cameraPosition), u_cameraClipping.x, u_cameraClipping.y), 1.0);");
    }

    private static void buildSpriteFragmentShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
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
        } else if (colorField.getFieldType() == ShaderFieldType.Vector4) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector3) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector2) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildParticlesFragmentShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Fragment part
        if (!vertexShaderBuilder.hasVaryingVariable("v_deathTime")) {
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
        } else if (colorField.getFieldType() == ShaderFieldType.Vector4) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector3) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector2) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildScreenFragmentShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
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
        } else if (colorField.getFieldType() == ShaderFieldType.Vector4) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector3) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector2) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildModelFragmentShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
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
        } else if (colorField.getFieldType() == ShaderFieldType.Vector4) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector3) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Vector2) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }

    private static void buildModelVertexShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Vertex part
        int boneCount = GraphShaderConfig.getMaxNumberOfBonesPerMesh();
        int boneWeightCount = GraphShaderConfig.getMaxNumberOfBoneWeights();
        vertexShaderBuilder.addArrayUniformVariable("u_bones", boneCount, "mat4", false, new UniformSetters.Bones(boneCount));
        for (int i = 0; i < boneWeightCount; i++) {
            vertexShaderBuilder.addAttributeVariable(VertexAttribute.BoneWeight(i), "a_boneWeight" + i, "vec2");
        }
        StringBuilder getSkinning = new StringBuilder();
        getSkinning.append("mat4 getSkinning() {\n");
        getSkinning.append("  mat4 skinning = mat4(0.0);\n");
        for (int i = 0; i < boneWeightCount; i++) {
            getSkinning.append("  skinning += (a_boneWeight").append(i).append(".y) * u_bones[int(a_boneWeight").append(i).append(".x)];\n");
        }
        getSkinning.append("  return skinning;\n");
        getSkinning.append("}\n");

        vertexShaderBuilder.addFunction("getSkinning", getSkinning.toString());

        vertexShaderBuilder.addMainLine("mat4 skinning = getSkinning();");

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, modelConfigurations);
        if (positionField == null) {
            vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");

            vertexShaderBuilder.addMainLine("// Attribute Position Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
            String name = "result_defaultPositionAttribute";
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = (u_worldTrans * skinning * vec4(a_position, 1.0)).xyz;");

            positionField = new DefaultFieldOutput(ShaderFieldType.Vector3, name);
        }
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        String worldPosition = "vec4(" + positionField.getRepresentation() + ", 1.0)";
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * " + worldPosition + ";");
    }

    private static void applyAlphaDiscard(FragmentShaderBuilder fragmentShaderBuilder, GraphShaderNodeBuilder.FieldOutput alphaField, String alpha, GraphShaderNodeBuilder.FieldOutput alphaClipField, String alphaClip) {
        if (alphaField != null || alphaClipField != null) {
            fragmentShaderBuilder.addMainLine("// End Graph Node");
            fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
            fragmentShaderBuilder.addMainLine("  discard;");
        }
    }

    private static void buildSpriteVertexShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(0), ShaderProgram.TEXCOORD_ATTRIBUTE + 0, "vec2");

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        GraphShaderNodeBuilder.FieldOutput layerField = getOutput(findInputVertices(graph, "end", "layer"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        GraphShaderNodeBuilder.FieldOutput anchorField = getOutput(findInputVertices(graph, "end", "anchor"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        GraphShaderNodeBuilder.FieldOutput sizeField = getOutput(findInputVertices(graph, "end", "size"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        GraphShaderNodeBuilder.FieldOutput rotationField = getOutput(findInputVertices(graph, "end", "rotation"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, spriteConfigurations);
        if (positionField == null) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 2, "a_position"), "a_position", "vec2");
            positionField = new DefaultFieldOutput(ShaderFieldType.Vector2, "a_position");
        }
        if (layerField == null) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_layer"), "a_layer", "float");

            String name = "result_defaultLayerAttribute";
            vertexShaderBuilder.addMainLine("float " + name + " = a_layer;");

            layerField = new DefaultFieldOutput(ShaderFieldType.Float, name);
        }
        if (sizeField == null) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 2, "a_size"), "a_size", "vec2");
            sizeField = new DefaultFieldOutput(ShaderFieldType.Vector2, "a_size");
        } else if (sizeField.getFieldType() == ShaderFieldType.Float) {
            sizeField = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(" + sizeField.getRepresentation() + ")");
        }
        if (anchorField == null) {
            vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 2, "a_anchor"), "a_anchor", "vec2");
            anchorField = new DefaultFieldOutput(ShaderFieldType.Vector2, "a_anchor");
        } else if (anchorField.getFieldType() == ShaderFieldType.Float) {
            anchorField = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(" + anchorField.getRepresentation() + ")");
        }
        vertexShaderBuilder.addUniformVariable("u_cameraUp", "vec3", true, UniformSetters.cameraUp);
        vertexShaderBuilder.addUniformVariable("u_cameraDirection", "vec3", true, UniformSetters.cameraDirection);
        vertexShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition);

        String billboardPosition = "result_billboardPosition";
        vertexShaderBuilder.addMainLine("vec3 result_cameraRight = normalize(cross(u_cameraDirection, u_cameraUp));");
        vertexShaderBuilder.addMainLine("vec3 result_cameraUp = normalize(u_cameraUp);");
        String size = sizeField.getRepresentation();
        vertexShaderBuilder.addMainLine("float result_xAdjust = " + size + ".x * (a_texCoord0.x - " + anchorField.getRepresentation() + ".x);");
        vertexShaderBuilder.addMainLine("float result_yAdjust = " + size + ".y * (a_texCoord0.y - " + anchorField.getRepresentation() + ".y);");
        if (rotationField != null) {
            String rotation = rotationField.getRepresentation();
            vertexShaderBuilder.addMainLine("float result_rotatedX = result_xAdjust * cos(" + rotation + ") - result_yAdjust * sin(" + rotation + ");");
            vertexShaderBuilder.addMainLine("float result_rotatedY = result_xAdjust * sin(" + rotation + ") + result_yAdjust * cos(" + rotation + ");");
        } else {
            vertexShaderBuilder.addMainLine("float result_rotatedX = result_xAdjust;");
            vertexShaderBuilder.addMainLine("float result_rotatedY = result_yAdjust;");
        }
        vertexShaderBuilder.addMainLine("vec3 xBillboardPosition = (" + positionField + ".x + result_rotatedX) * result_cameraRight;");
        vertexShaderBuilder.addMainLine("vec3 yBillboardPosition = (" + positionField + ".y - result_rotatedY) * result_cameraUp;");
        vertexShaderBuilder.addMainLine("vec3 zBillboardPosition = vec3(0.0, 0.0, u_cameraPosition.z) + normalize(u_cameraDirection) * " + layerField.getRepresentation() + ";");
        vertexShaderBuilder.addMainLine("vec3 " + billboardPosition + " = xBillboardPosition + yBillboardPosition + zBillboardPosition;");
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        String worldPosition = "vec4(" + billboardPosition + ", 1.0)";
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * " + worldPosition + ";");
    }

    private static void buildParticlesVertexShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 1, "a_seed"), "a_seed", "float");
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(1024, 1, "a_birthTime"), "a_birthTime", "float");
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(2048, 1, "a_deathTime"), "a_deathTime", "float");
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.TexCoords(0), ShaderProgram.TEXCOORD_ATTRIBUTE + 0, "vec2");

        ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new ObjectMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertices(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        GraphShaderNodeBuilder.FieldOutput sizeField = getOutput(findInputVertices(graph, "end", "size"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        GraphShaderNodeBuilder.FieldOutput rotationField = getOutput(findInputVertices(graph, "end", "rotation"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, particleConfigurations);
        if (positionField == null) {
            vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");

            vertexShaderBuilder.addMainLine("// Attribute Position Node");
            String name = "result_defaultPositionAttribute";
            vertexShaderBuilder.addMainLine("vec3 " + name + " = a_position;");

            positionField = new DefaultFieldOutput(ShaderFieldType.Vector3, name);
        }
        if (sizeField == null) {
            sizeField = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(0.1)");
        } else if (sizeField.getFieldType() == ShaderFieldType.Float) {
            sizeField = new DefaultFieldOutput(ShaderFieldType.Vector2, "vec2(" + sizeField.getRepresentation() + ")");
        }
        vertexShaderBuilder.addUniformVariable("u_cameraUp", "vec3", true, UniformSetters.cameraUp);
        vertexShaderBuilder.addUniformVariable("u_cameraDirection", "vec3", true, UniformSetters.cameraDirection);
        String billboardPosition = "result_billboardPositionAttribute";
        vertexShaderBuilder.addMainLine("vec3 result_cameraRight = cross(u_cameraDirection, u_cameraUp);");
        String size = sizeField.getRepresentation();
        vertexShaderBuilder.addMainLine("float result_xAdjust = " + size + ".x * (a_texCoord0.x - 0.5);");
        vertexShaderBuilder.addMainLine("float result_yAdjust = " + size + ".y * (a_texCoord0.y - 0.5);");
        if (rotationField != null) {
            String rotation = rotationField.getRepresentation();
            vertexShaderBuilder.addMainLine("float result_rotatedX = result_xAdjust * cos(" + rotation + ") - result_yAdjust * sin(" + rotation + ");");
            vertexShaderBuilder.addMainLine("float result_rotatedY = result_xAdjust * sin(" + rotation + ") + result_yAdjust * cos(" + rotation + ");");
        } else {
            vertexShaderBuilder.addMainLine("float result_rotatedX = result_xAdjust;");
            vertexShaderBuilder.addMainLine("float result_rotatedY = result_yAdjust;");
        }
        vertexShaderBuilder.addMainLine("vec3 result_rightAdjust = result_rotatedX * normalize(result_cameraRight);");
        vertexShaderBuilder.addMainLine("vec3 result_downAdjust = result_rotatedY * normalize(-u_cameraUp);");
        vertexShaderBuilder.addMainLine("vec3 " + billboardPosition + " = " + positionField.getRepresentation() + " + (result_rightAdjust + result_downAdjust);");
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans);
        String worldPosition = "vec4(" + billboardPosition + ", 1.0)";
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * " + worldPosition + ";");
    }

    private static void buildScreenVertexShader(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph, boolean designTime, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        // Vertex part
        vertexShaderBuilder.addAttributeVariable(VertexAttribute.Position(), ShaderProgram.POSITION_ATTRIBUTE, "vec3");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = vec4((a_position.xy * 2.0 - 1.0), 1.0, 1.0);");
    }

    private static GraphShaderNodeBuilder.FieldOutput getOutput(Array<GraphConnection> connections,
                                                                boolean designTime, boolean fragmentShader,
                                                                Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
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
            Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
            GraphShaderContext context, GraphShader graphShader, String nodeId, ObjectMap<String, ObjectMap<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphConfiguration... graphConfigurations) {
        ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> nodeOutput = nodeOutputs.get(nodeId);
        if (nodeOutput == null) {
            GraphNode<ShaderFieldType> nodeInfo = graph.getNodeById(nodeId);
            String nodeInfoType = nodeInfo.getConfiguration().getType();
            GraphShaderNodeBuilder nodeBuilder = getNodeBuilder(nodeInfoType, graphConfigurations);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfoType);
            ObjectMap<String, Array<GraphShaderNodeBuilder.FieldOutput>> inputFields = new ObjectMap<>();
            for (GraphNodeInput<ShaderFieldType> nodeInput : new ObjectMap.Values<>(nodeBuilder.getConfiguration(nodeInfo.getData()).getNodeInputs())) {
                String fieldId = nodeInput.getFieldId();
                Array<GraphConnection> vertexInfos = findInputVertices(graph, nodeId, fieldId);
                if (vertexInfos.size == 0 && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                Array<ShaderFieldType> fieldTypes = new Array<>();
                Array<GraphShaderNodeBuilder.FieldOutput> fieldOutputs = new Array<>();
                for (GraphConnection vertexInfo : vertexInfos) {
                    ObjectMap<String, GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder, graphConfigurations);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    fieldTypes.add(fieldType);
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

    private static ObjectSet<String> findRequiredOutputs(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                         String nodeId) {
        ObjectSet<String> result = new ObjectSet<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId))
                result.add(vertex.getFieldFrom());
        }
        return result;
    }

    private static Array<GraphConnection> findInputVertices(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                            String nodeId, String nodeField) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                result.add(vertex);
        }
        return result;
    }

    private static GraphShaderPropertyProducer findPropertyProducerByType(ShaderFieldType type, GraphConfiguration... graphConfigurations) {
        for (GraphConfiguration configuration : graphConfigurations) {
            for (GraphShaderPropertyProducer graphShaderPropertyProducer : configuration.getPropertyProducers()) {
                if (graphShaderPropertyProducer.getType() == type)
                    return graphShaderPropertyProducer;
            }
        }

        return null;
    }
}
