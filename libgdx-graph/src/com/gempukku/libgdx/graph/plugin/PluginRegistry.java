package com.gempukku.libgdx.graph.plugin;

public interface PluginRegistry {
    <T> void registerPublicData(Class<T> clazz, T value);

    <T extends RuntimePipelinePlugin> void registerPrivateData(Class<T> clazz, T value);
}
