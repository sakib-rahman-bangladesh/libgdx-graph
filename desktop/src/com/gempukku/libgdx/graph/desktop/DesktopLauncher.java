package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.Lighting3DPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.design.SpritesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.ui.design.UIPluginDesignInitializer;
import com.gempukku.libgdx.graph.ui.LibgdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.PluginDefinition;
import com.gempukku.libgdx.graph.ui.plugin.PluginRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class DesktopLauncher {
    public static void main(String[] arg) throws IOException, URISyntaxException, InterruptedException {
        String executeArgument = "NoPlugins";
        if (arg.length == 0 || !arg[0].equals(executeArgument)) {
            String jarPath = DesktopLauncher.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            System.out.println("Starting process...");

            String[] args = new String[]{"java", "-javaagent:" + jarPath, "-jar", jarPath, executeArgument};
            ProcessBuilder builder = new ProcessBuilder(args);

            Process process = builder.start();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                String line;
                while ((line = errorReader.readLine()) != null)
                    System.out.println(line);
            } finally {
                errorReader.close();
            }
        } else {
            System.out.println("Starting libGDX-graph designer");

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

            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.width = 1440;
            config.height = 810;
            new LwjglApplication(new LibgdxGraphApplication(), config);
        }
    }
}
