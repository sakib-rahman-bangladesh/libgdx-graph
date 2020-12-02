package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class ShaderLoaderCallback extends GraphDataLoaderCallback<GraphShader, ShaderFieldType> {
    private Texture defaultTexture;
    private boolean screen;
    private boolean depthShader;
    private GraphConfiguration[] graphConfigurations;

    public ShaderLoaderCallback(Texture defaultTexture, boolean screen, GraphConfiguration... graphConfiguration) {
        this(defaultTexture, screen, false, graphConfiguration);
    }

    public ShaderLoaderCallback(Texture defaultTexture, boolean screen, boolean depthShader, GraphConfiguration... graphConfiguration) {
        this.defaultTexture = defaultTexture;
        this.screen = screen;
        this.depthShader = depthShader;
        graphConfigurations = graphConfiguration;
    }

    @Override
    public void start() {

    }

    @Override
    public GraphShader end() {
        GraphValidator<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        if (screen) {
            return GraphShaderBuilder.buildScreenShader(defaultTexture, this, false);
        } else {
            if (depthShader)
                return GraphShaderBuilder.buildModelDepthShader(defaultTexture, this, false);
            else
                return GraphShaderBuilder.buildModelShader(defaultTexture, this, false);
        }
    }

    @Override
    protected ShaderFieldType getFieldType(String type) {
        return ShaderFieldType.valueOf(type);
    }

    @Override
    protected NodeConfiguration<ShaderFieldType> getNodeConfiguration(String type, JsonValue data) {
        for (GraphConfiguration graphConfiguration : graphConfigurations) {
            GraphShaderNodeBuilder graphShaderNodeBuilder = graphConfiguration.getGraphShaderNodeBuilders().get(type);
            if (graphShaderNodeBuilder != null)
                return graphShaderNodeBuilder.getConfiguration(data);
        }

        return null;
    }
}
