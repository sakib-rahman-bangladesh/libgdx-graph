package com.gempukku.libgdx.graph.plugin.models;

import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.plugin.models.producer.ModelShaderRendererPipelineNodeProducer;

public class ModelsPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private static int maxNumberOfBonesPerMesh;
    private static int maxNumberOfBoneWeights;

    public static void register() {
        register(12, 5);
    }

    public static void register(
            int maxNumberOfBonesPerMesh, int maxNumberOfBoneWeights) {
        ModelsPluginRuntimeInitializer.maxNumberOfBonesPerMesh = maxNumberOfBonesPerMesh;
        ModelsPluginRuntimeInitializer.maxNumberOfBoneWeights = maxNumberOfBoneWeights;
        PluginRegistryImpl.register(ModelsPluginRuntimeInitializer.class);
    }

    private GraphModelsImpl data;

    public ModelsPluginRuntimeInitializer() {
        data = new GraphModelsImpl(maxNumberOfBonesPerMesh, maxNumberOfBoneWeights);
    }

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        RendererPipelineConfiguration.register(new ModelShaderRendererPipelineNodeProducer(pluginRegistry, maxNumberOfBonesPerMesh, maxNumberOfBoneWeights));

        pluginRegistry.registerPrivateData(GraphModelsImpl.class, data);
        pluginRegistry.registerPublicData(GraphModels.class, data);
    }

    @Override
    public void dispose() {
        data.dispose();
    }
}
