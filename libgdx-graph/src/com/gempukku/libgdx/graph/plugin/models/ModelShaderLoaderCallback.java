package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.loader.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class ModelShaderLoaderCallback extends GraphDataLoaderCallback<ModelGraphShader, ShaderFieldType> {
    private Texture defaultTexture;
    private int maxBoneCount;
    private int maxBoneWeightCount;
    private boolean depthShader;
    private GraphConfiguration[] graphConfigurations;

    public ModelShaderLoaderCallback(Texture defaultTexture, int maxBoneCount, int maxBoneWeightCount,
                                     boolean depthShader, GraphConfiguration... graphConfiguration) {
        this.defaultTexture = defaultTexture;
        this.maxBoneCount = maxBoneCount;
        this.maxBoneWeightCount = maxBoneWeightCount;
        this.depthShader = depthShader;
        graphConfigurations = graphConfiguration;
    }

    @Override
    public void start() {

    }

    @Override
    public ModelGraphShader end() {
        GraphValidator<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        if (depthShader)
            return GraphShaderBuilder.buildModelDepthShader(defaultTexture, maxBoneCount, maxBoneWeightCount, this, false);
        else
            return GraphShaderBuilder.buildModelShader(defaultTexture, maxBoneCount, maxBoneWeightCount, this, false);
    }

    @Override
    protected ShaderFieldType getFieldType(String type) {
        return ShaderFieldTypeRegistry.findShaderFieldType(type);
    }

    @Override
    protected NodeConfiguration<ShaderFieldType> getNodeConfiguration(String type, JsonValue data) {
        for (GraphConfiguration graphConfiguration : graphConfigurations) {
            GraphShaderNodeBuilder graphShaderNodeBuilder = graphConfiguration.getGraphShaderNodeBuilder(type);
            if (graphShaderNodeBuilder != null)
                return graphShaderNodeBuilder.getConfiguration(data);
        }

        return null;
    }
}
