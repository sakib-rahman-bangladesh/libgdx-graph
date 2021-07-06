package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRegistryImpl;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.AmbientLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ApplyNormalMapShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.BlinnPhongLightingShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.DirectionalLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.PhongLightingShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.PointLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.SpotLightShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;

public class Lighting3DPluginRuntimeInitializer implements PluginRuntimeInitializer {
    private static int maxNumberOfDirectionalLights;
    private static int maxNumberOfPointLights;
    private static int maxNumberOfSpotlights;

    public static void register() {
        register(5, 2, 2);
    }

    public static void register(int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights) {
        Lighting3DPluginRuntimeInitializer.maxNumberOfDirectionalLights = maxNumberOfDirectionalLights;
        Lighting3DPluginRuntimeInitializer.maxNumberOfPointLights = maxNumberOfPointLights;
        Lighting3DPluginRuntimeInitializer.maxNumberOfSpotlights = maxNumberOfSpotlights;
        PluginRegistryImpl.register(Lighting3DPluginRuntimeInitializer.class);
    }

    private Lighting3DPrivateData data = new Lighting3DPrivateData();

    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        CommonShaderConfiguration.register(new BlinnPhongLightingShaderNodeBuilder(maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights));
        CommonShaderConfiguration.register(new PhongLightingShaderNodeBuilder(maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights));
        CommonShaderConfiguration.register(new ApplyNormalMapShaderNodeBuilder());
        CommonShaderConfiguration.register(new AmbientLightShaderNodeBuilder());
        CommonShaderConfiguration.register(new DirectionalLightShaderNodeBuilder());
        CommonShaderConfiguration.register(new PointLightShaderNodeBuilder());
        CommonShaderConfiguration.register(new SpotLightShaderNodeBuilder());

        pluginRegistry.registerPrivateData(Lighting3DPrivateData.class, data);
        pluginRegistry.registerPublicData(Lighting3DPublicData.class, data);
    }

    @Override
    public void dispose() {

    }
}
