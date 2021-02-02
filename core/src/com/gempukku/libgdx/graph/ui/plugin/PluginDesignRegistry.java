package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class PluginDesignRegistry {
    private static ObjectSet<Class<? extends PluginDesignInitializer>> plugins = new ObjectSet<>();

    public static void register(Class<? extends PluginDesignInitializer> plugin) {
        plugins.add(plugin);
    }

    public static void initializePlugins() throws ReflectionException {
        for (Class<? extends PluginDesignInitializer> plugin : plugins) {
            PluginDesignInitializer pluginDesignInitializer = ClassReflection.newInstance(plugin);
            pluginDesignInitializer.initialize();
        }
    }
}
