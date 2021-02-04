package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class PluginRegistry {
    private static Array<PluginDefinition> pluginDefinitionArray = new Array<>();

    public static void addPluginDefinition(PluginDefinition pluginDefinition) {
        pluginDefinitionArray.add(pluginDefinition);
    }

    public static Iterable<PluginDefinition> getPluginDefinitions() {
        return pluginDefinitionArray;
    }

    public static void initializePlugins() throws ReflectionException {
        for (PluginDefinition plugin : pluginDefinitionArray) {
            Class<? extends PluginDesignInitializer> pluginClass = plugin.pluginClass;
            if (pluginClass != null) {
                try {
                    PluginDesignInitializer pluginDesignInitializer = ClassReflection.newInstance(pluginClass);
                    pluginDesignInitializer.initialize();
                    plugin.loaded = true;
                } catch (Exception exp) {
                    Gdx.app.error("Plugins", "Unable to load plugin - " + plugin.pluginName, exp);
                }
            }
        }
    }
}
