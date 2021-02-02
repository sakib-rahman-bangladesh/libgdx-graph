package com.gempukku.libgdx.graph.plugin;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class PluginRegistryImpl implements PluginRegistry, Disposable {
    private static ObjectSet<Class<? extends PluginRuntimeInitializer>> plugins = new ObjectSet<>();

    public static void register(Class<? extends PluginRuntimeInitializer> clazz) {
        plugins.add(clazz);
    }

    public static PluginRegistryImpl initializePlugins() throws ReflectionException {
        PluginRegistryImpl result = new PluginRegistryImpl();
        for (Class<? extends PluginRuntimeInitializer> plugin : plugins) {
            PluginRuntimeInitializer pluginRuntimeInitializer = ClassReflection.newInstance(plugin);
            pluginRuntimeInitializer.initialize(result);
            result.resources.add(pluginRuntimeInitializer);
        }
        return result;
    }

    private ObjectSet<Disposable> resources = new ObjectSet<>();
    private ObjectMap<String, Object> privateData = new ObjectMap<>();
    private ObjectMap<String, Object> publicData = new ObjectMap<>();

    @Override
    public <T> void registerPublicData(Class<T> clazz, T value) {
        publicData.put(clazz.getName(), value);
    }

    @Override
    public <T> void registerPrivateData(Class<T> clazz, T value) {
        privateData.put(clazz.getName(), value);
    }

    public <T> T getPrivateData(Class<T> clazz) {
        return (T) privateData.get(clazz.getName());
    }

    public <T> T getPublicData(Class<T> clazz) {
        return (T) publicData.get(clazz.getName());
    }

    @Override
    public void dispose() {
        for (Disposable resource : resources) {
            resource.dispose();
        }
    }
}
