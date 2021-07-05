package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.Lighting3DPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.maps.design.MapsPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.design.SpritesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.ui.design.UIPluginDesignInitializer;
import com.gempukku.libgdx.graph.ui.LibgdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.PluginDefinition;
import com.gempukku.libgdx.graph.ui.plugin.PluginPreferences;
import com.gempukku.libgdx.graph.ui.plugin.PluginRegistry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DesktopLauncher {
    public static void main(String[] arg) throws IOException {
        setupPlugins();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1440;
        config.height = 810;
        new LwjglApplication(new LibgdxGraphApplication(), config);
    }

    private static void setupPlugins() throws MalformedURLException {
        setupPluginClassLoader();

        // Built-in plugins
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", UIPluginDesignInitializer.class,
                        "UI rendering", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", ScreenPluginDesignInitializer.class,
                        "Screen shaders", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", SpritesPluginDesignInitializer.class,
                        "Sprite shaders", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", ParticlesPluginDesignInitializer.class,
                        "Particle shaders", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", ModelsPluginDesignInitializer.class,
                        "Model shaders", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", Lighting3DPluginDesignInitializer.class,
                        "3D Lighting", "latest", false, false));
        PluginRegistry.addPluginDefinition(
                new PluginDefinition("internal", MapsPluginDesignInitializer.class,
                        "Maps renderer (Tiled, etc)", "latest", false, false));

        readExtraPlugins();
    }

    private static void readExtraPlugins() {
        for (String plugin : PluginPreferences.getPlugins()) {
            File pluginFile = new File(plugin);
            PluginDefinition pluginDefinition = new PluginDefinition(
                    plugin, null, "", "", false, true);
            if (pluginFile.exists()) {
                try {
                    pluginDefinition = PluginPreferences.getPluginDefinition(pluginFile);
                } catch (Exception exp) {
                    System.out.println("Unable to load plugin from file - " + plugin);
                    exp.printStackTrace();
                }
            }
            PluginRegistry.addPluginDefinition(pluginDefinition);
        }
    }

    private static void setupPluginClassLoader() throws MalformedURLException {
        Array<URL> pluginUrls = new Array<>(URL.class);
        for (String plugin : PluginPreferences.getPlugins()) {
            FileHandle pluginJar = new FileHandle(new File(plugin));
            if (pluginJar.exists()) {
                pluginUrls.add(pluginJar.file().toURI().toURL());
            }
        }
        URLClassLoader classLoader = URLClassLoader.newInstance(pluginUrls.toArray(), Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(classLoader);
    }
}
